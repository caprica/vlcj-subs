vlcj-subs
=========

Optional vlcj library for integrating subtitle/sub-picture functionality into Java applications.

Why? There is no way to get sub-title text from VLC through the LibVLC API, but sometimes it would be beneficial to be
able to render sub-titles in a Java application rather than relying on VLC to do it.

This project provides a simple SRT parser and a component that can trigger events that an application can use to show
sub-titles at the appropriate time.

In future other parsers for different sub-title file formats may be added.

News
====

- 9th June, 2019 - new project

Documentation is currently limited but the API is very small and quite simple - please look at the test sources for
example usage.

All releases are at available at [Maven Central](https://search.maven.org/search?q=a:vlcj-subs).

You can follow @capricasoftware on Twitter for more vlcj news.

Maven Dependency
----------------

Add the following Maven dependency to your own project pom.xml:

```
<dependency>
    <groupId>uk.co.caprica</groupId>
    <artifactId>vlcj-subs</artifactId>
    <version>0.1.0</version>
</dependency>
```

Related Projects
----------------

 * [vlcj](https://github.com/caprica/vlcj)

License
-------

The vlcj framework is provided under the GPL, version 3 or later.

If you want to consider a commercial license for vlcj that allows you to use and redistribute vlcj without complying
with the GPL then send an email to the address below:

> mark [dot] lee [at] capricasoftware [dot] co [dot] uk

Contributors
------------

Contributions are welcome and will always be licensed according to the Open Source license terms of the project (currently GPL).

However, for a contribution to be accepted you must agree to transfer any copyright so that your contribution does not
impede our ability to provide commercial licenses for vlcj.
