sed -i "s|<localRepository.*>|<localRepository>$PWD/repo</localRepository>|g" settings.xml

if [ -z "$XXL_EXECUTOR_EXTERNAL_PORT" ] || [ -z "$XXL_EXECUTOR_INTERNAL_PORT" ] || [ -z "$XXL_EXECUTOR_IP" ]; then
echo -e 'enviroment unset, exit \r\n set like \r\n export XXL_EXECUTOR_EXTERNAL_PORT=10098 \r\n export XXL_EXECUTOR_INTERNAL_PORT=6006 \r\n export XXL_EXECUTOR_IP=192.168.3.61'
exit -1
else 
echo 'enviroment set'
fi

if [ -z "$XXL_EXECUTOR_LOG_ID" ]; then
echo -e 'LOG_ID NOT SET, set like \r\n export XXL_EXECUTOR_LOG_ID=1'
XXL_EXECUTOR_LOG_ID=$RANDOM
echo -e "RANDOM LOG_ID $XXL_EXECUTOR_LOG_ID"
else 
echo 'log id set'
fi

sed -i "s|xxl.job.executor.port.*$|xxl.job.executor.port=$XXL_EXECUTOR_INTERNAL_PORT|g" xxl-job-executor-samples/xxl-job-executor-sample-springboot/src/main/resources/application.properties
sed -i "s|xxl.job.executor.address.*$|xxl.job.executor.address=http://$XXL_EXECUTOR_IP:$XXL_EXECUTOR_EXTERNAL_PORT|g" xxl-job-executor-samples/xxl-job-executor-sample-springboot/src/main/resources/application.properties
mvn install -s settings.xml

cd xxl-job-executor-samples/xxl-job-executor-sample-springboot
mv target "target-temp/target.$XXL_EXECUTOR_LOG_ID"
java -jar "target-temp/target.$XXL_EXECUTOR_LOG_ID/xxl-job-executor-sample-springboot-2.3.1-SNAPSHOT.jar" > "target-temp/executor.log.$XXL_EXECUTOR_LOG_ID" 2>&1 &
