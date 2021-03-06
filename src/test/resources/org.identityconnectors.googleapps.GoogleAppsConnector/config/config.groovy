/**
 * ====================
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2008-2009 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2011-2013 Tirasa. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License("CDDL") (the "License"). You may not use this file
 * except in compliance with the License.
 *
 * You can obtain a copy of the License at https://oss.oracle.com/licenses/CDDL
 * See the License for the specific language governing permissions and limitations
 * under the License.
 *
 * When distributing the Covered Code, include this CDDL Header Notice in each file
 * and include the License file at https://oss.oracle.com/licenses/CDDL.
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * ====================
 */
import org.identityconnectors.contract.data.groovy.Lazy

testsuite {
    bundleJar=System.getProperty("bundleJar")
    bundleName=System.getProperty("bundleName")
    bundleVersion=System.getProperty("bundleVersion")
    connectorName="org.identityconnectors.googleapps.GoogleAppsConnector"

    Validate.invalidConfig = [
      [connectionUrl: null],
      [connectionUrl: ""],
      [connectionUrl: "invalidUrl"],
      [login: null],
      [login: ""],
      [domain: null],
      [domain: ""],
      [password: null],
      [password: ""],
    ]

    Test.invalidConfig = [
      [connectionUrl: "http://something.com"],
      [login: "bogus"],
      [domain: "bogus"],
      [password: "password"]
    ]

    Schema {
        strictCheck = false
        oclasses=['__ACCOUNT__', '__GROUP__']
        attributes.__ACCOUNT__.oclasses=['__NAME__', 'familyName', 'givenName', 'quota', 'nicknames', 'groups', '__PASSWORD__', '__ENABLE__']
        attributes.__GROUP__.oclasses=['__NAME__', 'members', 'owners', 'groupName', 'groupDescription', 'groupPermissions']
        // account and group attributes are supported by all operations, no need to test this
        operations = [:]
    }
}

connector {
    connectionUrl = "__CONFIGURE_ME__"
    login = "__CONFIGURE_ME__"
    domain = "__CONFIGURE_ME__"
    password = "__CONFIGURE_ME__"

    // for unit testing only
    testGroup = "connectorstest@identityconnectors.com"
}


// attribute values:
name = Lazy.random("contr\\act###", String.class)
__ACCOUNT__.__NAME__ = Lazy.get("name")
__ACCOUNT__.familyName = Lazy.get("name")
__ACCOUNT__.givenName = Lazy.get("name")
// googleApps returns them in alphabetical order
__ACCOUNT__.nicknames = [Lazy.random("baaaa##"), Lazy.random("caaaa##")]
// it seems that 25600 is the only accepted quota value for this domain, all other values are defaulted by google apps to 25600
__ACCOUNT__.quota = 25600
// assume that there are no existing groups on the resource
__ACCOUNT__.groups = []
__GROUP__.__NAME__ = Lazy.random("contr\\act###", String.class) + "@" + Lazy.get("connector.domain")
__GROUP__.members = [Lazy.get("connector.login") + "@" + Lazy.get("connector.domain"), Lazy.random("contr\\act###", String.class) + "@" + Lazy.get("connector.domain")]
// must already exist
__GROUP__.owners = [Lazy.get("connector.login") + "@" + Lazy.get("connector.domain")]
__GROUP__.groupPermissions = "Domain"