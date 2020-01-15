#include <iostream>
#include <libyang/libyang.h>
#include <stdlib.h>

using namespace std;

string openconfigInterfacesInterfaces =
    "{\n"
    "  \"openconfig-interfaces:interfaces\": {\n"
    "    \"interface\": [\n"
    "      {\n"
    "        \"config\": {\n"
    "          \"description\": \"This is ifc  0/2\",\n"
    "          \"enabled\": true,\n"
    "          \"mtu\": 1500,\n"
    "          \"name\": \"0/2\",\n"
    "          \"type\": \"iana-if-type:ethernetCsmacd\"\n"
    "        },\n"
    "        \"name\": \"0/2\",\n"
    "        \"state\": {\n"
    "          \"admin-status\": \"UP\",\n"
    "          \"counters\": {\n"
    "            \"in-broadcast-pkts\": 2767640,\n"
    "            \"in-discards\": 0,\n"
    "            \"in-errors\": 0,\n"
    "            \"in-multicast-pkts\": 5769311,\n"
    "            \"in-octets\": 427066814515,\n"
    "            \"in-unicast-pkts\": 293117,\n"
    "            \"out-broadcast-pkts\": 7628,\n"
    "            \"out-discards\": 0,\n"
    "            \"out-errors\": 0,\n"
    "            \"out-multicast-pkts\": 325039,\n"
    "            \"out-octets\": 72669652,\n"
    "            \"out-unicast-pkts\": 125182\n"
    "          },\n"
    "          \"description\": \"Some descr\",\n"
    "          \"enabled\": true,\n"
    "          \"mtu\": 1518,\n"
    "          \"name\": \"0/2\",\n"
    "          \"oper-status\": \"DOWN\",\n"
    "          \"type\": \"iana-if-type:ethernetCsmacd\"\n"
    "        }\n"
    "      },\n"
    "      {\n"
    "        \"config\": {\n"
    "          \"description\": \"This is ifc  0/3\",\n"
    "          \"enabled\": true,\n"
    "          \"mtu\": 1500,\n"
    "          \"name\": \"0/3\",\n"
    "          \"type\": \"iana-if-type:ethernetCsmacd\"\n"
    "        },\n"
    "        \"name\": \"0/3\",\n"
    "        \"state\": {\n"
    "          \"admin-status\": \"UP\",\n"
    "          \"counters\": {\n"
    "            \"in-broadcast-pkts\": 2767641,\n"
    "            \"in-discards\": 0,\n"
    "            \"in-errors\": 0,\n"
    "            \"in-multicast-pkts\": 5769311,\n"
    "            \"in-octets\": 427066814515,\n"
    "            \"in-unicast-pkts\": 293117,\n"
    "            \"out-broadcast-pkts\": 7628,\n"
    "            \"out-discards\": 0,\n"
    "            \"out-errors\": 0,\n"
    "            \"out-multicast-pkts\": 325039,\n"
    "            \"out-octets\": 72669652,\n"
    "            \"out-unicast-pkts\": 125182\n"
    "          },\n"
    "          \"enabled\": true,\n"
    "          \"mtu\": 1518,\n"
    "          \"name\": \"0/3\",\n"
    "          \"oper-status\": \"DOWN\",\n"
    "          \"type\": \"iana-if-type:ethernetCsmacd\"\n"
    "        }\n"
    "      },\n"
    "      {\n"
    "        \"config\": {\n"
    "          \"description\": \"This is ifc  0/4\",\n"
    "          \"enabled\": true,\n"
    "          \"mtu\": 1500,\n"
    "          \"name\": \"0/4\",\n"
    "          \"type\": \"iana-if-type:ethernetCsmacd\"\n"
    "        },\n"
    "        \"name\": \"0/4\",\n"
    "        \"state\": {\n"
    "          \"admin-status\": \"UP\",\n"
    "          \"counters\": {\n"
    "            \"in-broadcast-pkts\": 2767642,\n"
    "            \"in-discards\": 0,\n"
    "            \"in-errors\": 0,\n"
    "            \"in-multicast-pkts\": 5769311,\n"
    "            \"in-octets\": 427066814515,\n"
    "            \"in-unicast-pkts\": 293117,\n"
    "            \"out-broadcast-pkts\": 7628,\n"
    "            \"out-discards\": 0,\n"
    "            \"out-errors\": 0,\n"
    "            \"out-multicast-pkts\": 325039,\n"
    "            \"out-octets\": 72669652,\n"
    "            \"out-unicast-pkts\": 125182\n"
    "          },\n"
    "          \"enabled\": true,\n"
    "          \"mtu\": 1518,\n"
    "          \"name\": \"0/4\",\n"
    "          \"oper-status\": \"DOWN\",\n"
    "          \"type\": \"iana-if-type:ethernetCsmacd\"\n"
    "        }\n"
    "      }\n"
    "    ]\n"
    "  }\n"
    "}";

int main() {

  ly_ctx *pLyCtx = ly_ctx_new("./yang", 0);
  ly_ctx_load_module(pLyCtx, "iana-if-type", NULL);
  const lys_module *pModule =
      ly_ctx_load_module(pLyCtx, "openconfig-interfaces", NULL);

  lyd_node *pNode = lyd_parse_mem(
      pLyCtx, const_cast<char *>(openconfigInterfacesInterfaces.c_str()),
      LYD_JSON, LYD_OPT_DATA | LYD_OPT_DATA_NO_YANGLIB);

  string path = "/openconfig-interfaces:interfaces/interface[name='0/3']/state";
  ly_set *pSet = lyd_find_path(pNode, const_cast<char *>(path.c_str()));
  if (pSet->number != 1) {
    return 1; 
  }

  char *buff;
  lyd_print_mem(&buff, pSet->set.d[0], LYD_JSON, 0);
  std::cout << buff << std::endl;

  free(buff);

  return 0;
}
