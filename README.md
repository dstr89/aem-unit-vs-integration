# AEM: Unit vs Integration testing

## About

This is an example AEM project demonstrating how to make use of JUnit, AEM Mocks and Mockito for writing integration tests.
Instead of writing separate unit tests for each class, in this approach we focus on testing the expected functionality gained from integrating those classes.
We step away from the unit/code level testing and focus our test on what the end user is actually expecting as the output (acceptance criteria).

## Use case

In this example we implemented a component called _Article list_ that fetches and displays information from its child pages.

We could define acceptance criteria for the components as following:
* Once added to a home page, the component automatically fetches all child article pages
* For every fetched article, the component displays
    * Title linking to the article page (on publisher)
    * Thumbnail of the first images used in the article (319x319 px)
    * Description consisting of the article text (up to 250 characters followed by three dots)   

## Implementation

To conform to best practices, we implemented a Sling model for the component `ArticleListModel.java`, but separated all the business logic into dedicated OSGi service.
Thus `LinkService`, `ImageFinderService` and `TextExcerptsService` are all small services with single responsibility, that could be easily reused for other use cases in the future.

## Testing

When writing an integration test we focus on the end result (output) and not on internal implementation. In case of an AEM component, the class outputting results to HTML is our Sling Model.
That means we only need to test the `ArticleListModel.java` class. However, in our test we will use real implementations of all internal services, so they get tested as well.
It is important to note that in `ArticleListModelTest.java` we mock all AEM internal services like `SlingSettingsService`, `Externalizer` and `XSSAPI`, but use real implementation for all custom services like `LinkService`, `ImageFinderService` and `TextExcerptsService`.

Next, we load the sample content from JSON files and write a single integration test that will cover our model, services and the defined acceptance criteria:
* GIVEN that two articles are present in the content under the home page
* WHEN article list component model is adapted from the home page
* THEN the model returns two articles with their corresponding title, description, URL and image

## Coverage

The 20 LOC we wrote for the integration test achieved code coverage of 95%, measured by jacoco plugin. Coverage report can be reached at:

    core/target/site/jacoco/index.html
    
Of course, additional unit test could be written for individual services to cover them even better. 
Decide for yourself if that makes sense in a particular case or not.

## Modules

The main project modules are:

* core: Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as component-related Java code such as servlets or request filters.
* ui.apps: contains the /apps (and /etc) parts of the project, ie JS&CSS clientlibs, components, templates, runmode specific configs
* ui.content: contains sample content using the components from the ui.apps

## How to build

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

If you have a running AEM instance you can build and package the whole project and deploy into AEM with

    mvn clean install -PautoInstallPackage

Or to deploy it to a publish instance, run

    mvn clean install -PautoInstallPackagePublish

Or alternatively

    mvn clean install -PautoInstallPackage -Daem.port=4503

Or to deploy only the bundle to the author, run

    mvn clean install -PautoInstallBundle

## Maven settings

The project comes with the auto-public repository configured. To setup the repository in your Maven settings, refer to:

    http://helpx.adobe.com/experience-manager/kb/SetUpTheAdobeMavenRepository.html

When building the project you can point to a specific Maven setting file to use like this:
    
    mvn -s /Users/hrzz003c/.m2/settings_adobe.xml clean install -PautoInstallPackage