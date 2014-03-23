(ns zugradar.core
  (:require [clj-http.client :as client])
  (:use compojure.core)
  (:require [compojure.route :as route])
  (:use ring.adapter.jetty)
  (:gen-class))
   
(def train-general "http://zugradar.oebb.at/bin/query.exe/dny?look_minx=8071655&look_maxx=18497681&look_miny=45443394&look_maxy=50442313&tpl=trains2json2&look_productclass=15&look_json=yes&performLocating=1&look_nv=get_zntrainname|yes|attr|81|get_rtonly|yes|zugposmode|2|interval|30000|intervalstep|2000|maxnumberoftrains|500|")

(def train-detail "http://zugradar.oebb.at/bin/query.exe/dny?tpl=singletrain2json&performLocating=8&&look_nv=get_rtmsgstatus|yes|get_zntrainname|yes|get_rtfreitextmn|yes|get_product|yes| get_realstops|yes|get_rtstoptimes|yes|get_fstop|yes|get_pstop|yes|get_nstop|yes|get_lstop|yes|zugposmode|2|&look_trainid=")

(defn get-json [url] 
  (let [{:keys [body headers]} (client/get url)] (println headers) body))



(defroutes app
  (GET "/train" [] (get-json train-general))
  
  (GET "/detail/:id" [id]
       (get-json
         (str train-detail 
              (clojure.string/replace id #"-" "/"))))
  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))

(defn -main [& args]
  (run-jetty 
    app  
    {:port 8080 :join? false}))
  