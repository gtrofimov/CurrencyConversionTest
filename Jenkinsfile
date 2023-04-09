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
        when { equals expected: true, actual: true }
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
                
                copyArtifacts(projectName: 'currency-exchange-service-jtest');
                copyArtifacts(projectName: 'currency-conversion-service-jtest');

                sh  '''
                    for file in $PWD/*.zip; do
                        filename=$(basename "${file}")
                        newfile="${filename%.*}"
                        unzip ${file} ${newfile:16:4}
                    done
                    '''
                sh  '''
                    # for dir in $PWD/runtime_cov*; do
                        #port = ${dir:16:4}
                        #for file2 in ${PWD}
                        #run jtest command
                    '''
                
                
            }

        }
    }
}