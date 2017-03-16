#!/usr/bin/env bash

set -e

ENV="development"
IMAGE_NAME="mdm-backend-image"
IMAGE_VERSION="1.1"
CONTAINER_NAME="mdm.backend"
HOST_PORT="8080"
CONTAINER_PORT="8080"
STOP=false
BUILD=false
START=false
PATH_TO_DOCKERFILE=`cd "$(dirname "$0")"; pwd`

function unsetDockerEnv() {
  unset DOCKER_TLS_VERIFY
  unset DOCKER_CERT_PATH
  unset DOCKER_MACHINE_NAME
  unset DOCKER_HOST
}

function stopAndRemoveContainer(){
        docker stop "$CONTAINER_NAME" || true && docker rm -f "$CONTAINER_NAME" || true
}


case "$1" in
    "stop")
      STOP=true
      ;;
    "start")
      START=true
      ;;
    "build")
      BUILD=true
      ;;
    "restart")
      STOP=true
      START=true
      ;;
    "rebuild")
      STOP=true
      BUILD=true
      START=true
      ;;
    *)
      echo -en "\033[37;1;41mПорядок использования: ${0##*/} <command> <environment> <image_name> <image_version> <container_name> <host_port> <container_port_to_expose>.\033[0m\n"
      exit 65
      ;;
esac

case "$2" in
  ""|"dev"|"develop"|"development")
    ENV="development"
    unsetDockerEnv
    echo -en "\033[33mDEFAULT ENV:\033[0m\033[35m $ENV\033[0m\n"
    ;;
  "stage"|"staging"|"integration")
    ENV="integration"
    res=eval $(docker-machine env $ENV)
    HOST_PORT=$CONTAINER_PORT
    echo -en "\033[32mENV\033[0m: \033[35m$ENV\033[0m\n"
    ;;
  "prod"|"production")
    ENV="production"
    res=eval $(docker-machine env $ENV)
    HOST_PORT=$CONTAINER_PORT
    echo -en "\033[32mENV\033[0m: \033[35m$ENV\033[0m\n"
    ;;
  *)
    echo -en "\033[37;1;41mПорядок использования: ${0##*/} <environment> <image_name> <image_version> <container_name> <host_port> <container_port_to_expose>.\033[0m\n"
    exit 65
    ;;
esac

case "$3" in
    "")
      echo -en "\033[33mDEFAULT IMAGE_NAME:\033[0m\033[35m $IMAGE_NAME\033[0m\n"
      ;;
    *)
        IMAGE_NAME=$3
        echo -en "\033[32mIMAGE_NAME\033[0m: \033[35m$IMAGE_NAME\033[0m\n"
esac

case "$4" in
    "")
      echo -en "\033[33mDEFAULT IMAGE_VERSION: \033[0m\033[35m $IMAGE_VERSION\033[0m\n"
      ;;
    *)
        IMAGE_VERSION=$4
        echo -en "\033[32mIMAGE_VERSION\033[0m: \033[35m$IMAGE_VERSION\033[0m\n"
esac

case "$5" in
    "")
      echo -en "\033[33mDEFAULT CONTAINER_NAME:\033[0m\033[35m $CONTAINER_NAME\033[0m\n"
      ;;
    *)
        CONTAINER_NAME=$5
        echo -en "\033[32mCONTAINER_NAME\033[0m: \033[35m$CONTAINER_NAME\033[0m\n"
esac

case "$6" in
  "")
    echo -en "\033[33mDEFAULT HOST_PORT:\033[0m\033[35m $HOST_PORT\033[0m\n"
    ;;
  [0-9][0-9][0-9][0-9])
      HOST_PORT=$6
      echo -en "\033[32mHOST_PORT\033[0m: \033[35m$HOST_PORT\033[0m\n"
esac

case "$7" in
  "")
    echo -en "\033[33mDEFAULT CONTAINER_PORT:\033[0m\033[35m $CONTAINER_PORT\033[0m\n"
    ;;
  [0-9][0-9][0-9][0-9])
      HOST_PORT=$7
      echo -en "\033[32mCONTAINER_PORT\033[0m = \033[35m$CONTAINER_PORT\033[0m\n"
esac



if "$BUILD";
then
    mvn clean install -Dmaven.test.skip=true
    rsync -avz target/*.jar "$(dirname "$0")"/.tmp/
    cd $PATH_TO_DOCKERFILE
    docker build -t "$IMAGE_NAME:$IMAGE_VERSION" .
    rm -rf .tmp
fi

if "$STOP";
then
    stopAndRemoveContainer
fi

if "$START";
then
  cd $PATH_TO_DOCKERFILE
  docker run --name "$CONTAINER_NAME" -p "$HOST_PORT:$CONTAINER_PORT" --link mdm.postgres:postgres --link mdm.redis:redis --link mdm.auth:auth -v /Users/vinichenkosa/Docker/vm/admin_module/file-storage:/file-storage -d "$IMAGE_NAME:$IMAGE_VERSION"
  #
  docker logs --follow $CONTAINER_NAME
fi



