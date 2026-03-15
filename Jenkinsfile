pipeline {
    agent any

    tools {
        maven 'Maven-3.8'  // 需要在Jenkins中配置Maven
        jdk 'JDK-11'       // 需要在Jenkins中配置JDK 11
    }

    environment {
        // 项目路径（Jenkins会自动拉取到workspace）
        PROJECT_PATH = "${WORKSPACE}"
        // Docker镜像前缀
        DOCKER_REGISTRY = "localhost"
        IMAGE_TAG = "${BUILD_NUMBER}"
    }

    stages {
        stage('拉取代码') {
            steps {
                echo '开始拉取代码...'
                // 从本地Git仓库拉取代码
                checkout scm
                echo '代码拉取完成'
            }
        }

        stage('编译打包') {
            steps {
                echo '开始Maven编译打包...'
                sh 'mvn clean package -DskipTests'
                echo '编译打包完成'
            }
        }

        stage('构建Docker镜像') {
            steps {
                echo '开始构建Docker镜像...'
                script {
                    // 构建各个微服务的Docker镜像
                    def services = ['user-service', 'product-service', 'cart-service',
                                    'order-service', 'message-service', 'gateway']

                    services.each { service ->
                        echo "构建 ${service} 镜像..."
                        sh """
                            cd ${service}
                            docker build -t ${DOCKER_REGISTRY}/${service}:${IMAGE_TAG} -f ../Dockerfile .
                            docker tag ${DOCKER_REGISTRY}/${service}:${IMAGE_TAG} ${DOCKER_REGISTRY}/${service}:latest
                        """
                    }
                }
                echo 'Docker镜像构建完成'
            }
        }

        stage('停止旧容器') {
            steps {
                echo '停止旧容器...'
                sh '''
                    docker-compose -f docker-compose-full.yml stop || true
                '''
                echo '旧容器已停止'
            }
        }

        stage('启动新容器') {
            steps {
                echo '启动新容器...'
                sh '''
                    docker-compose -f docker-compose-full.yml up -d --build
                '''
                echo '新容器启动完成'
            }
        }

        stage('健康检查') {
            steps {
                echo '等待服务启动...'
                sleep 30
                echo '检查服务状态...'
                sh '''
                    docker-compose -f docker-compose-full.yml ps
                '''
            }
        }
    }

    post {
        success {
            echo '✅ 部署成功！'
            // 可以添加通知，比如发送邮件或钉钉消息
        }
        failure {
            echo '❌ 部署失败！'
            // 可以添加失败通知
        }
        always {
            echo '清理工作空间...'
            // 清理临时文件
            cleanWs()
        }
    }
}
