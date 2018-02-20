#!/usr/bin/bash

javac -cp .:"jar/*" TemplateEngineTest.java
java -cp .:"jar/*" org.junit.runner.JUnitCore TemplateEngineTest