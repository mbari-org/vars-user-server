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

import org.mbari.kb.core.MiscFactory;
import org.mbari.kb.core.UserAccount;

/**
 * Created by IntelliJ IDEA.
 * User: brian
 * Date: Aug 19, 2009
 * Time: 3:29:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class MiscFactoryImpl implements MiscFactory {

    public UserAccount newUserAccount() {
        return new UserAccountImpl();
    }
}
