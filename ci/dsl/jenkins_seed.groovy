import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*

// Each jenkins_seed will only differ by a a few variables
def component_name="tm-api-event-seats"
def component_org="com.tm.api"
def github_repo_url = "https://github.com/danny-huertas/${component_name}"
def github_repo_ssh_uri = "git@github.com:danny-huertas/${component_name}.git"
def github_build_tools_url = "https://github.org/danny-huertas/tm-api-build-tools"
def github_ssh_cred = "d498e5d9-f7a3-4550-933c-395297b74606"
def application_port = "3020"

// Repository details
def artifactory_user_var = "repo_user"
def artifactory_password_var = "repo_password"
def artifactory_cred = "ac152582-4330-4b3d-b657-45da87598bbe"
def artifactory_virtual_repo = "tm-virtual"

def sonar_cred = "6c291877-597c-433e-963b-7bfd71db4d62"

// The following shouldn't really change between components
def jenkins_build_path="builds"
def org_name_upper="TM"
def build_nodes = "javaBuilds"
def deploy_nodes = "deployServers"
def jenkins_github_cred = "9b83ca6b-0cad-4d78-b3e4-8f7b513f053d"
def java_version = "java-8-openjdk"
def gradle_name = "Gradle 4.2.1"
def project_prefix="${component_name}"
def deploy_job_name="${project_prefix}_deploy"
def build_allbranches_job_name="${project_prefix}_build_allbranches"
def build_dev_job_name = "${project_prefix}_build_publish_dev"
def build_master_job_name = "${project_prefix}_build_publish_release"

def deploy_dev_cred = "3ee75952-e4f5-475b-ac17-f1ff13066681"
def deploy_qa_cred = "6862d73e-eadd-4481-8302-2733ee1d416c"

def git_user = "jenkins"
def git_email = "dannyhue@gmail.com"
def default_email = "dannyhue@gmail.com"

// master build and release
freeStyleJob("${jenkins_build_path}/${org_name_upper}/${build_master_job_name}") {
    description("Build and release the master branch of ${component_name}")
    scm {
        git {
            remote {
                url("${github_repo_ssh_uri}")
                credentials("${github_ssh_cred}")
                branches("master")
            }
            extensions {
                messageExclusion {
                    excludedMessage('.*\\[Gradle Release Plugin\\].*')
                }
                localBranch("master")
                wipeWorkspace()
            }
        }
    }

    parameters {
        booleanParam('RELEASE_BUILD', false, 'Perform a release build.')
        stringParam('RELEASE_VERSION', '1.0.0', 'Release version to publish. (Unused if not performing a release build)')
        stringParam('NEXT_DEVELOPMENT_VERSION', '1.0.1-SNAPSHOT', 'Release development version to publish. This should end with -SNAPSHOT. (Unused if not performing a release build)')
    }

    triggers {
        scm("H/10 * * * *")
    }

    // configure job properties section
    configure { project ->
        // configure purge logrotate plugin
        project / 'properties' << 'jenkins.model.BuildDiscarderProperty' {
            strategy (class: 'hudson.tasks.LogRotator') {
                daysToKeep(2)
                numToKeep(20)
                artifactDaysToKeep(-1)
                artifactNumToKeep(-1)
            }
        }

        // configure Slave Utilization Plugin
        project / 'properties' << 'com.suryagaddipati.jenkins.SlaveUtilizationProperty' {
            needsExclusiveAccessToNode(false)
            singleInstancePerSlave(false)
            slaveUtilizationPercentage(0)
        }
    }

    // restrict where this project can be run
    label("${build_nodes}")

    // configure java/jdk compiler
    jdk("${java_version}")

    wrappers {
        maskPasswords()
        credentialsBinding {
            usernamePassword("${artifactory_user_var}", "${artifactory_password_var}", "${artifactory_cred}")
        }
        sshAgent("${github_ssh_cred}")
        environmentScript {
            script(
                    '#!/bin/bash\n' +
                            'if [[ "${RELEASE_BUILD}" = "true" ]]; then\n' +
                            'echo GRADLE_TARGET=release\n' +
                            'else\n' +
                            'echo GRADLE_TARGET="build publish"\n' +
                            'fi\n'
            )
            scriptType('unixScript')
            runOnlyOnParent(false)
            hideEnvironmentVariablesValues(false)
        }
    }

    steps {
        shell (
                "git config user.name \"${git_user}\" \n" +
                        "git config user.email \"${git_email}\" "
        )
        gradle {
            tasks("-Partifactory_contextUrl=\${JAVA_ARTIFACTORY_URL} -Partifactory_user=\${repo_user} -Partifactory_password=\${repo_password} -Partifactory_apikey=\${repo_password} -Prelease.useAutomaticVersion=true -Prelease.releaseVersion=\${RELEASE_VERSION} -Prelease.newVersion=\${NEXT_DEVELOPMENT_VERSION} clean \${GRADLE_TARGET}")
            gradleName("${gradle_name}")
            useWrapper(false)
        }
    }

    publishers {
        mailer("${default_email}", true, true)
    }
}

// DI build and publish
freeStyleJob("${jenkins_build_path}/${org_name_upper}/${build_dev_job_name}") {
    description("Build the dev branch of ${component_name} and publish a snapshot to artifactory")
    scm {
        git {
            remote {
                url("${github_repo_url}")
                credentials("${jenkins_github_cred}")
                branches("dev")
                configure { node ->
                    node / 'extensions' << 'hudson.plugins.git.extensions.impl.MessageExclusion' {
                        excludedMessage '.*\\[artifactory-release\\].*'
                    }
                }
            }
        }
    }

    // configure job properties section
    configure { project ->
        // configure purge logrotate plugin
        project / 'properties' << 'jenkins.model.BuildDiscarderProperty' {
            strategy (class: 'hudson.tasks.LogRotator') {
                daysToKeep(2)
                numToKeep(20)
                artifactDaysToKeep(-1)
                artifactNumToKeep(-1)
            }
        }

        // configure Slave Utilization Plugin
        project / 'properties' << 'com.suryagaddipati.jenkins.SlaveUtilizationProperty' {
            needsExclusiveAccessToNode(false)
            singleInstancePerSlave(false)
            slaveUtilizationPercentage(0)
        }
    }

    // restrict where this project can be run
    label("${build_nodes}")

    // configure java/jdk compiler
    jdk("${java_version}")

    triggers {
        scm("H/10 * * * *")
    }

    wrappers {
        maskPasswords()
        credentialsBinding {
            usernamePassword("${artifactory_user_var}", "${artifactory_password_var}", "${artifactory_cred}")
            usernamePassword("sonar_url", "sonar_password", "${sonar_cred}")
        }
    }

    steps {
        gradle {
            tasks("-Partifactory_contextUrl=\${JAVA_ARTIFACTORY_URL} -Partifactory_user=\${repo_user} -Partifactory_password=\${repo_password} -Psonar_host=\${sonar_url} -Psonar_username=\${sonar_password} clean build publish sonar")
            gradleName("${gradle_name}")
            useWrapper(false)
        }
    }
}

// all branches build and test, no published artifacts to Artifactory
freeStyleJob("${jenkins_build_path}/${org_name_upper}/${build_allbranches_job_name}") {
    description("Build all branches of ${component_name} and run tests but does not publish a snapshot to artifactory")
    scm {
        git {
            remote {
                url("${github_repo_url}")
                credentials("${jenkins_github_cred}")
                branches("**")
                configure { node ->
                    node / 'extensions' << 'hudson.plugins.git.extensions.impl.MessageExclusion' {
                        excludedMessage '.*\\[Gradle Release Plugin\\].*'
                    }
                }
            }
        }
    }

    // configure job properties section
    configure { project ->
        // configure purge logrotate plugin
        project / 'properties' << 'jenkins.model.BuildDiscarderProperty' {
            strategy (class: 'hudson.tasks.LogRotator') {
                daysToKeep(2)
                numToKeep(20)
                artifactDaysToKeep(-1)
                artifactNumToKeep(-1)
            }
        }

        // configure Slave Utilization Plugin
        project / 'properties' << 'com.suryagaddipati.jenkins.SlaveUtilizationProperty' {
            needsExclusiveAccessToNode(false)
            singleInstancePerSlave(false)
            slaveUtilizationPercentage(0)
        }
    }

    // restrict where this project can be run
    label("${build_nodes}")

    // configure java/jdk compiler
    jdk("${java_version}")

    triggers {
        scm("H/10 * * * *")
    }

    wrappers {
        maskPasswords()
        credentialsBinding {
            usernamePassword("${artifactory_user_var}", "${artifactory_password_var}", "${artifactory_cred}")
        }
    }

    steps {
        gradle {
            tasks("-Partifactory_contextUrl=\${JAVA_ARTIFACTORY_URL} -Partifactory_user=\${repo_user} -Partifactory_password=\${repo_password} -Partifactory_apikey=\${repo_password} -PartifactoryPublish.skip=true clean build")
            gradleName("${gradle_name}")
            useWrapper(false)
        }
    }

    publishers {
        mailer("${default_email}", true, true)
    }
}

// Deploy job
freeStyleJob("${jenkins_build_path}/${org_name_upper}/${deploy_job_name}") {

    scm {
        git {
            remote {
                url("${github_build_tools_url}")
                credentials("${jenkins_github_cred}")
                branches("master")
                configure { node ->
                    node / 'extensions' << 'hudson.plugins.git.extensions.impl.MessageExclusion' {
                        excludedMessage '.*\\[artifactory-release\\].*'
                    }
                }
            }
        }
    }

    // configure job properties section
    configure { project ->
        // configure purge logrotate plugin
        project / 'properties' << 'jenkins.model.BuildDiscarderProperty' {
            strategy (class: 'hudson.tasks.LogRotator') {
                daysToKeep(2)
                numToKeep(20)
                artifactDaysToKeep(-1)
                artifactNumToKeep(-1)
            }
        }

        // configure Slave Utilization Plugin
        project / 'properties' << 'com.suryagaddipati.jenkins.SlaveUtilizationProperty' {
            needsExclusiveAccessToNode(false)
            singleInstancePerSlave(false)
            slaveUtilizationPercentage(0)
        }

    }

    // parameterize the build
    parameters {
        choiceParam('env', ['dev', 'qa'], 'select which env for configserver spring profile')
        stringParam('ARTIFACT_VERSION', '1.0.1', 'Artifact version')
    }

    // restrict where this project can be run
    label("${deploy_nodes}")

    // configure java/jdk compiler
    jdk("${java_version}")

    // configure build wrappers section
    configure { project ->
        // cleanup workspace before build
        project / buildWrappers / 'hudson.plugins.ws__cleanup.PreBuildCleanup' {
            deleteDirs(false)
            cleanupParameter()
            externalDelete()
        }

        // Add timestamps to the Console Output Plugin
        project / buildWrappers / 'hudson.plugins.timestamper.TimestamperBuildWrapper' {
        }

        // Mask passwords (and enable global passwords)
        project / buildWrappers / 'com.michelin.cio.hudson.plugins.maskpasswords.MaskPasswordsBuildWrapper' {
            varPasswordPairs {
                varPasswordPair()
            }
        }

        // enable build user environment variables
        project / buildWrappers / 'org.jenkinsci.plugins.builduser.BuildUser' {
        }

        project / buildWrappers / 'org.jenkinsci.plugins.credentialsbinding.impl.SecretBuildWrapper' {
            bindings {
                'org.jenkinsci.plugins.credentialsbinding.impl.SSHUserPrivateKeyBinding' {
                    credentialsId "${deploy_dev_cred}"
                    keyFileVariable "di_keyfile"
                    usernameVariable "di_username"
                }
                'org.jenkinsci.plugins.credentialsbinding.impl.SSHUserPrivateKeyBinding' {
                    credentialsId "${deploy_qa_cred}"
                    keyFileVariable "qa_keyfile"
                    usernameVariable "qa_username"
                }
                'org.jenkinsci.plugins.credentialsbinding.impl.UsernamePasswordMultiBinding' {
                    credentialsId "${artifactory_cred}"
                    usernameVariable "${artifactory_user_var}"
                    passwordVariable "${artifactory_password_var}"
                }
            }
        }
    }

    wrappers {
        maskPasswords()
    }

    steps {
        shell (
                '#!/bin/bash\n' +
                        '# revise deployment script with env variables\n' +
                        'WORKSPACE_DEPLOY_SCRIPT=${WORKSPACE}/deploy_app.sh \n' +
                        'WORKSPACE_TEMPLATE_SERVICE=${WORKSPACE}/deploy/template.service \n' +
                        'REMOTE_SCRIPT=${JOB_BASE_NAME}_${BUILD_ID}.sh \n' +
                        'echo "ARTIFACT_VERSION=${ARTIFACT_VERSION}" > $WORKSPACE_DEPLOY_SCRIPT \n' +
                        'echo "ARTIFACT_ID=' + component_name + '" >> $WORKSPACE_DEPLOY_SCRIPT \n' +
                        'echo "env=${env}" >> $WORKSPACE_DEPLOY_SCRIPT \n' +
                        'echo "ARTIFACTORY_USER=${repo_user}" >> $WORKSPACE_DEPLOY_SCRIPT \n' +
                        'echo "ARTIFACTORY_PASSWORD=\'"${repo_password}"\'" >> $WORKSPACE_DEPLOY_SCRIPT \n' +
                        'echo "ARTIFACTORY_BASE_URL=${JAVA_ARTIFACTORY_URL}' + artifactory_virtual_repo + '" >> $WORKSPACE_DEPLOY_SCRIPT \n' +
                        'echo "ARTIFACT_ORG=' + component_org + '" >> $WORKSPACE_DEPLOY_SCRIPT \n' +
                        'echo "APPLICATION_PORT=' + application_port + '" >> $WORKSPACE_DEPLOY_SCRIPT \n' +
                        'echo "JAVA_EXTRA_ARGS=-Djava.io.tmpdir=/var/tmp" >> $WORKSPACE_DEPLOY_SCRIPT \n' +
                        'cat $WORKSPACE/deploy/deploy_spring_boot_service.sh >> $WORKSPACE_DEPLOY_SCRIPT \n' +
                        '\n' +
                        'if [[ "${env}" = "dev" ]]; then\n' +
                        'echo "deploying to DI"\n' +
                        'SERVER_DEV=dev-tm-all.westus.com\n' +
                        'scp -oStrictHostKeyChecking=no -i ${di_keyfile} $WORKSPACE_DEPLOY_SCRIPT ${di_username}@$SERVER_DEV:/tmp/${REMOTE_SCRIPT}\n' +
                        'scp -oStrictHostKeyChecking=no -i ${di_keyfile} $WORKSPACE_TEMPLATE_SERVICE ${di_username}@$SERVER_DEV:/tmp/template.service\n' +
                        'ssh -oStrictHostKeyChecking=no -i ${di_keyfile} -tt ${di_username}@$SERVER_DEV sudo su -c "\'chmod +x /tmp/${REMOTE_SCRIPT}; cd /tmp; ./${REMOTE_SCRIPT}\'" || exit 1\n' +
                        'fi\n' +
                        'if [[ "${env}" = "qa" ]]; then\n' +
                        'echo "deploying to QA1"\n' +
                        'SERVER_QA1=qa-tm-all1.westus.com\n' +
                        'scp -oStrictHostKeyChecking=no -i ${qa_keyfile} $WORKSPACE_DEPLOY_SCRIPT ${qa_username}@$SERVER_QA1:/tmp/${REMOTE_SCRIPT}\n' +
                        'scp -oStrictHostKeyChecking=no -i ${qa_keyfile} $WORKSPACE_TEMPLATE_SERVICE ${qa_username}@$SERVER_QA1:/tmp/template.service\n' +
                        'ssh -oStrictHostKeyChecking=no -i ${qa_keyfile} -tt ${qa_username}@$SERVER_QA1 sudo su -c "\'chmod +x /tmp/${REMOTE_SCRIPT}; cd /tmp; ./${REMOTE_SCRIPT}\'" || exit 1\n' +

                        'echo "deploying to QA2"\n' +
                        'SERVER_QA2=qa-tm-all2.westus.com\n' +
                        'scp -oStrictHostKeyChecking=no -i ${qa_keyfile} $WORKSPACE_DEPLOY_SCRIPT ${qa_username}@$SERVER_QA2:/tmp/${REMOTE_SCRIPT}\n' +
                        'scp -oStrictHostKeyChecking=no -i ${qa_keyfile} $WORKSPACE_TEMPLATE_SERVICE ${qa_username}@$SERVER_QA2:/tmp/template.service\n' +
                        'ssh -oStrictHostKeyChecking=no -i ${qa_keyfile} -tt ${qa_username}@$SERVER_QA2 sudo su -c "\'chmod +x /tmp/${REMOTE_SCRIPT}; cd /tmp; ./${REMOTE_SCRIPT}\'" || exit 1\n' +

                        'fi\n' +
                        '\n'
        )
    }
}

// create the job under a specific view
listView("${jenkins_build_path}/${org_name_upper}/${component_name}") {
    description("${org_name_upper} - ${component_name} related jobs")
    filterBuildQueue(true)
    filterExecutors(true)
    recurse(true)

    // list of jobs to be included in the View
    jobs {
        name(build_dev_job_name)
        name(build_master_job_name)
        name(build_allbranches_job_name)
        name(deploy_job_name)
    }

    // Job Display Status
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}