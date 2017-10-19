package org.wso2.security.staticscanner.scanners;

import org.apache.maven.shared.invoker.MavenInvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.security.staticscanner.Constants;
import org.wso2.security.staticscanner.NotificationManager;
import org.wso2.security.staticscanner.handlers.FileHandler;
import org.wso2.security.staticscanner.handlers.MavenHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

/*
*  Copyright (c) ${date}, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

public class DependencyCheckScanner {

    //Maven Commands
    private static final String MVN_COMMAND_DEPENDENCY_CHECK = "org.owasp:dependency-check-maven:check";
    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyCheckScanner.class);

    public static void startScan() {
        try {

            LOGGER.info("Dependency Check started");
            NotificationManager.notifyDependencyCheckStatus("running");

            MavenHandler.runMavenCommand(MainScanner.getProductPath() + File.separator + Constants.POM_FILE, MVN_COMMAND_DEPENDENCY_CHECK);

            if (new File(Constants.REPORTS_FOLDER_PATH).exists() || new File(Constants.REPORTS_FOLDER_PATH).mkdir()) {
                String reportsFolderPath = Constants.REPORTS_FOLDER_PATH + File.separator + Constants.DEPENDENCY_CHECK_REPORTS_FOLDER;
                FileHandler.findFilesAndMoveToFolder(MainScanner.getProductPath(), reportsFolderPath, Constants.DEPENDENCY_CHECK_REPORT);
            }
        } catch (IOException | MavenInvocationException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            NotificationManager.notifyDependencyCheckStatus("failed");
        }
    }
}
