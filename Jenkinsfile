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
        
        // Parasoft Licenses
        ls_url="${PARASOFT_LS_URL}"
        ls_user="${PARASOFT_LS_USER}"
        ls_pass="${PARASOFT_LS_PASS}"
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
                    
                    # Jtest Step
                    # Set Up and write .properties file
                    echo $"
                    parasoft.eula.accepted=true
                    jtest.license.use_network=true
                    jtest.license.network.edition=server_edition
                    license.network.use.specified.server=true
                    license.network.auth.enabled=true
                    license.network.url=${ls_url}
                    license.network.user=${ls_user}
                    license.network.password=${ls_pass}" >> jtest/jtestcli.properties
                    '''
                
                sh  '''
                    for file in $PWD/*.zip; do
                        unzip ${file} -d ${file: -8:-4}
                    done
                    '''
                sh  '''
                    for file in $PWD/monitor/*.xml; do
                        agent=${file: -8:-4}
                        
                        # run Jtest to generate report
                        docker run --rm -i \
                        -u 0:0 \
                        -v "$PWD:$PWD" \
                        -w "$PWD" \
                        $(docker build -q ./jtest) \
                        jtestcli \
                        -settings /home/parasoft/jtestcli.properties \
                        -staticcoverage "${file}" \
                        -runtimecoverage "${agent}/runtime_coverage" \
                        -config "jtest/CalculateApplicationCoverage.properties" \
                        -property report.coverage.images="${app_name}-ComponentTests" \
                        -property session.tag="ComponentTests"
                        -report ${agent}
                    done
                    '''
                
                
            }

        }
    }
}