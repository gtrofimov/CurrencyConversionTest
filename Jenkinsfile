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
        cov_agent_ports="8051,8052"
    }
    stages {
        stage('Build') {
        when { equals expected: true, actual: false }
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
        }
        stage('Report'){
            steps {
                // for each .zip {
                //    unzip 
                //    jtest gnerate report
                // }
                sh  '''
                    for file in $PWD/*.zip
                        echo ${file}
                    '''
                
                
            }

        }
    }
}