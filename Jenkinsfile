pipeline {
    agent any
    tools {
        maven 'maven'
        jdk 'JDK 17'
    }
    options {
        skipDefaultCheckout(true)
    }
    environment {
        allServices="http://exchange:8051, http://conversion:8052"
    }
    stages {
        stage('Build') {
            steps {
                cleanWs()
                checkout scm
                echo "Running Tests: ${env.JOB_NAME}"

                // start session 
                sh  '''
                    # Run tests
                    mvn clean test
                    '''
        
            }
            steps('Report') {
                script {
                    env.allServices.tokenize(",").each { url -> 
                                
                        sh  '''
                            echo $url
                            '''
                

                    }
                }
            }
        }
    }
}