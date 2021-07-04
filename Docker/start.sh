# Set $ngrok as an environment variable
echo "export ngrok=/Docker/ngrok" >> ~/.bashrc
echo "export CLASSPATH=.:$ngrok/lib:$ngrok/jre/lib:$CLASSPATH" >> ~/.bashrc
echo "export PATH=$ngrok/bin:$ngrok/jre/bin:$PATH" >> ~/.bashrc
source ~/.bashrc

# Move to /ProjectFiles
cd /$PROJECT

# Run Flask server with uWSGI
uwsgi /Docker/uWSGI.ini
