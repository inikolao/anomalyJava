#!/bin/bash
source /root/local/share/jubatus/jubatus.profile
jubaanomaly --configpath jubaanomaly-config.json --rpc-port $1&
