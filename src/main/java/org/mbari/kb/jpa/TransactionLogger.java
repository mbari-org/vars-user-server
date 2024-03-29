/*
 * Copyright 2017 Monterey Bay Aquarium Research Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mbari.kb.jpa;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author brian
 */
public class TransactionLogger {

    private static final Logger log = LoggerFactory.getLogger(TransactionLogger.class);

    public TransactionLogger() {
    }

    @PostLoad
    public void logLoad(Object object) {
        if (log.isDebugEnabled()) {
            log.debug("Loaded '{}' into persistent context", object);
        }
    }

    @PrePersist
    public void logPersist(Object object) {
        logTransaction(object, DAO.TransactionType.PERSIST);
    }

    @PreRemove
    public void logRemove(Object object) {
        logTransaction(object, DAO.TransactionType.REMOVE);
    }

    @PreUpdate
    public void logUpdate(Object object) {
        logTransaction(object, DAO.TransactionType.MERGE);
    }

    private void logTransaction(Object object, DAO.TransactionType transactionType) {
        if (log.isDebugEnabled()) {
            log.debug("Performing '{}' on {}", transactionType, object);
        }
    }
}