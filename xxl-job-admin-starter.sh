if [ -z "$XXL_EXECUTOR_ADMIN_ADDRESS" ] ; then
echo 'enviroment unset, exit, set like export XXL_EXECUTOR_ADMIN_ADDRESS=192.168.3.61:10098'
exit -1
else 
echo 'enviroment set'
sed -i "s|xxl.job.admin.addresses.*$|xxl.job.admin.addresses=http://$XXL_EXECUTOR_ADMIN_ADDRESS/xxl-job-admin|g" xxl-job-executor-samples/xxl-job-executor-sample-springboot/src/main/resources/application.properties

token=$RANDOM
sed -i "s|xxl.job.accessToken.*$|xxl.job.accessToken=$token|g" xxl-job-executor-samples/xxl-job-executor-sample-springboot/src/main/resources/application.properties
sed -i "s|xxl.job.accessToken.*$|xxl.job.accessToken=$token|g" xxl-job-admin/src/main/resources/application.properties
fi

sed -i "s|<localRepository.*>|<localRepository>$PWD/repo</localRepository>|g" settings.xml
mvn clean install -s settings.xml
cd xxl-job-admin
rm -rf target-temp
cp -r target target-temp
java -jar target-temp/xxl-job-admin-2.3.1-SNAPSHOT.jar > admin.console.log 2>&1 &
cd ..
