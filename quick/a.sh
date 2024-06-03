docker stop $(docker ps -aq) && docker rm -f $(docker ps -aq) && docker rmi -f $(docker images -q)

cd ~

rm -rf aigc

git clone -b serve https://gitee.com/jackie56678/aigc.git

