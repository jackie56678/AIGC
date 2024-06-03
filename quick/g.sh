mkdir -p /source/picture
mkdir -p /source/video

yum -y group install "Development Tools"

sudo yum update                 y

sudo yum install -y yum-utils device-mapper-persistent-data lvm2
sudo yum-config-manager --add-repo https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
sudo yum makecache fast
sudo yum -y install docker-ce
sudo systemctl start docker
docker version
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'

{
"registry-mirrors": ["https://e7n1ndig.mirror.aliyuncs.com"]
}
EOF

sudo systemctl daemon-reload
docker info
