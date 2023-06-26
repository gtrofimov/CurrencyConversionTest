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
                                
                // license jtest
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
                    license.network.password=${ls_pass}
                    jtest.unittest.xml.results.processing.enabled=true" >> jtest/jtestcli.properties
                    '''

                // start session 
                sh  '''
                    # Run tests
                    # mvn clean test
                    
                    # Run tests with Jtest
                    docker run --rm -i \
                    -u 995:991 \
                    -v "$PWD:$PWD" \
                    -w "$PWD" \
                    $(docker build -q ./jtest) /bin/bash -c " \
                    mvn \
                    -Dmaven.test.failure.ignore=true \
                    test-compile jtest:agent \
                    test jtest:jtest \
                    -s jtest/.m2/settings.xml \
                    -Djtest.settings='jtest/jtestcli.properties' \
                    -Djtest.config='builtin://Unit Tests'" \
                    -Djtest.forceTestReportsImport=true
                    '''       
            }
        }
        stage('Report'){
            steps {
                // copy static cov xmls
                copyArtifacts(projectName: 'currency-exchange-service-jtest');
                copyArtifacts(projectName: 'currency-conversion-service-jtest');
                
                // unzip coverages
                sh  '''
                    for file in $PWD/*.zip; do
                        unzip ${file} -d ${file: -8:-4}
                    done
                    '''
   
                // run jtest on all coverages
                sh  '''
                    # for every static_cov.xml
                    for file in $PWD/monitor/*.xml; do
                        # get the port from the last four charcters of static_cov.xml
                        cov_port=${file: -8:-4}
                        
                        # run Jtest to generate report
                        docker run --rm -i \
                        -u 995:991 \
                        -v "$PWD:$PWD" \
                        -w "$PWD" \
                        parasoft/jtest \
                        jtestcli \
                        -settings jtest/jtestcli.properties \
                        -staticcoverage "${file}" \
                        -runtimecoverage "${cov_port}/runtime_coverage" \
                        -config "jtest/CalculateApplicationCoverage.properties" \
                        -property report.coverage.images="${cov_port}-E2ETests" \
                        -property session.tag="E2ETests" \
                        -report ${cov_port}
                    done
                    '''               
            }

        }
    }
}