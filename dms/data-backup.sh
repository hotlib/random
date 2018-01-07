#!/bin/bash

# dir locations and commands

JETTY_POD_DIR=$(find /var/lib/kubelet/pods -name "dms-jetty")
JETTY_DATA_DIR=$(find "$JETTY_POD_DIR/../../" -name "pvc*")
SOLR_POD_DIR=$(find /var/lib/kubelet/pods -name "dms-solr")
SOLR_DATA_DIR=$(find "$SOLR_POD_DIR/../../" -name "pvc*")
SOLR_BACKUP="$SOLR_DATA_DIR/snapshot.backup"
LOCAL_BACKUP_DIR="/root/backup"
RSYNC_CMD='rsync -azrv --delete --progress -e "ssh -p 1022"'
BACKUP_SERVER="objectify@objectify.ddns.net"
BACKUP_SERVER_DIR="/home/objectify/backup"

# Service IPs

SOLR_IP="10.59.241.227"
MYSQL_IP="10.59.243.18"

SOLR_BACKUP_STATUS_QUERY="curl http://$SOLR_IP:8983/solr/develCore/replication?command=details"

echo "--------------dumping data----------------"

# mysql dump

mysqldump --all-databases -u dms --host $MYSQL_IP > "$LOCAL_BACKUP_DIR/dump.sql"

# solr backup

curl "http://$SOLR_IP:8983/solr/develCore/replication?command=backup&location=%2Fopt%2Fsolr%2Fserver%2Fsolr%2FdevelCore%2Fdata&name=backup"

# wait until SOLR finishes the backup

BACKUP_STATE="failure"
until [[ "$BACKUP_STATE" == "success" &&  ! -z "$COMPLETED_AT" ]]
do   
     BACKUP_STATE=$($SOLR_BACKUP_STATUS_QUERY | xmllint --format --xpath '//*[@name="backup"]/*[@name="status"]/text()' -)     
     COMPLETED_AT=$($SOLR_BACKUP_STATUS_QUERY | xmllint --format --xpath '//*[@name="backup"]/*[@name="snapshotCompletedAt"]/text()' -)     
     sleep 5s
     echo "waiting for SOLR backup to materialise .. (state: " $BACKUP_STATE ")"
done

date > $LOCAL_BACKUP_DIR/backup_start

echo "--------------copying data to backup server----------------"

# making sure content is really in sync
sync

# mv does not work, no idea why

cp -r $SOLR_BACKUP $LOCAL_BACKUP_DIR
rm -r $SOLR_BACKUP

eval "$RSYNC_CMD $LOCAL_BACKUP_DIR/backup_start $BACKUP_SERVER:$BACKUP_SERVER_DIR"                             # start time (logging purposes)
eval "$RSYNC_CMD $LOCAL_BACKUP_DIR/dump.sql $BACKUP_SERVER:$BACKUP_SERVER_DIR"                                 # MySql data
eval "$RSYNC_CMD $LOCAL_BACKUP_DIR/snapshot.backup $BACKUP_SERVER:$BACKUP_SERVER_DIR"                          # SOLR data (standard backup)
eval "$RSYNC_CMD --exclude \"lost+found\" $SOLR_DATA_DIR/ $BACKUP_SERVER:$BACKUP_SERVER_DIR/solr_data_dir"     # SOLR data (all data just to be safe)

eval "$RSYNC_CMD $JETTY_DATA_DIR/docRepo $BACKUP_SERVER:$BACKUP_SERVER_DIR"                                    # dms files

date > $LOCAL_BACKUP_DIR/backup_end

eval "$RSYNC_CMD $LOCAL_BACKUP_DIR/backup_end $BACKUP_SERVER:$BACKUP_SERVER_DIR"                               # stop time (logging purposes)

# cleanup

echo "--------------cleaning up the backup directory----------------"

rm $LOCAL_BACKUP_DIR/dump.sql
rm -r $LOCAL_BACKUP_DIR/snapshot.backup

