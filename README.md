# vars-user-server

![MBARI logo](src/site/resources/images/logo-mbari-3b.png)

Microservice that provides CRUD operations for user accounts and user preferences. Used in support of [vars-annotation](https://github.com/mbari-media-management/vars-annotation). Currently, this service shares the database used by [vars-kb](https://github.com/mbari-media-management/vars-kb) and [vars-kb-server](https://github.com/mbari-media-management/vars-kb-server). If needed, an organization could write a simple replacement; e.g. layer a web-service over an internal LDAP service.

This service is part of [MBARI's Media Management](https://mbari-media-management.github.io/) project.

__Requires Java 11__
