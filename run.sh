# Clone specified repo
git clone https://playlab.computing.ncku.edu.tw:4001/aica-spring-2020/aica_lab4.git

# Copy all files in the repo to /ProjectFiles
cp -r aica_lab4/. ProjectFiles

# Delete the repo folder
rm -rf aica_lab4

# Delete .git dir of the cloned repo
rm -rf ProjectFiles/.git

# Run the docker container
docker-compose up --build
