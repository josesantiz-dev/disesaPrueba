#!/bin/bash

extra_param=$1
scp "/mnt/javaz/data/DISESA/ERP/BASE/base de datos/"{$extra_param,quartz_$extra_param} sistemas@192.168.10.72:/opt/tmp/
exit 0