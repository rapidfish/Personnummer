# Personnummer API

## Java API to handle Swedish Personnummer


* Automatic checksum validation when parsing an input string to a Personnummer object

* Produces an Optional<Personnummer> object when parsing. If not valid it becomes an empty Optional

* toString() can be used as default output after parsing, but there is also four other methods to represent it

* compareTo() can be used to compare age between any two Personnummer

* Extract meta data from a Personnummer; age, gender, zodiac sign and sometimes even the place of birth (on region level)

* Create Personnummer - quickly generate any number of Personnummer (random, but yet valid), useful for testing purposes.

* Automatically resolving missing '-' separator  (or a '+' whan indicating an age above hundred years)

* When era is missing it gets calculated automatically (e.g. '89' becomes '1989', not '2089' using present date comparison.

* Input strings may have a leghts from 10 to 13 characters (e.g. '1212121212', '121212-1212', '201212121212', '20121212-1212')

* [Optional] Personnummer having 'invalid' checksums can be parsed by using a 'forgiving flag' with error correction.
	

## Example ...

Examples below show how to parse strings into Personnummer, using four equivalent versions of the same Personnummer. It uses different leghts for year, with and without the use of a delimiter (-).

```
Optional<Personnummer> pnrOpt = Personnummer.parse("4604300014");
```
or...
```
Optional<Personnummer> pnrOpt = Personnummer.parse("460430-0014");
```
or...
```
Optional<Personnummer> pnrOpt = Personnummer.parse("194604300014");
```
or...
```
Optional<Personnummer> pnrOpt = Personnummer.parse("19460430-0014");
```


validate ...
```
if(pnrOpt.isPresent()) {
	System.out.println("Personnummer is valid!");
} else {
	System.out.println("Personnummer is NOT valid!");
	System.exit(1);
}
```

Validate and create a Personnummer object, or else throw an exeption ...

```
Personnummer pnr = pnrOpt.orElseThrow(RuntimeException::new);
```

```
System.out.println("Gender: " + pnr.getGender("Woman", "Man")); // Man
System.out.println("Age " + pnr.getAgeNow() + " years old!"); // Age 73 years old!"
System.out.println("Birth date: " + pnr.getBirthDate()); // Birth date: 1946-04-30
System.out.println("Born on a: " + DateTimeFormat.forPattern("EEEE").print(pnr.getBirthDate())); // Born on a: Tuesday
System.out.println("Days since birth: " + pnr.getDaysSinceBirth()); // Days since birth: 26958
System.out.println("Personnummer checksum: " + pnr.getChecksum()); // Personnummer checksum: 4
System.out.println("Zodiac sign: " + IDHelper.getZodiacSign(pnr).getLatinName()); // Zodiac sign: Taurus

```

toString methods
```
System.out.println(pnr.toString10()); // 4604300014
System.out.println(pnr.toString11()); // 460430-0014, same as toString()
System.out.println(pnr.toString12()); // 194604300014, twelve digits having era (automatically) 
System.out.println(pnr.toString13()); // 19460430-0014, twelve digits, with era and '-' sign
```

## Example code (2)...
The API also supports the use of '+' sign, to indicate +100 years of age when Personnummer is written with only 10 digits (dates without era)...


```

	Personnummer pnr = Personnummer.parse("101201+0342").get(); // using + to indicate >100 years old.
		
	System.out.println(pnr.toString13()); // 19101201-0342, Correct era (19) automatically, regardless of input string. 
	System.out.println("Age: " + pnr.getAgeNow()); // Age: 109

```


## Set up
* Clone the project from bitbucket with Git or simply download it manually.
* Import the project as an "existing maven project" in your favourite IDE (e.g. Eclipse).
* Build using Maven from your IDE or from your command prompt (under the project root folder): **mvn clean install**
* ...or build using Maven plugin inside your favourite IDE (e.g. Eclipse) using a Maven plugin.
* When you receive "Build success" message from Maven, you are good to go! Start using it as a dependency in your own projects.
* To add the API in your project as a Maven dependency in you pom.xml, your pom.xml should look like this...

```
<dependencies>
   ...
   <dependency>
      <groupId>se.osbe.id</groupId>
      <artifactId>personnummer</artifactId>
      <version>0.4.0</version>
   </dependency>
   ...
</dependencies>

```

**Version 0.4.0**

Current stable branch is (not ready)

**Configuration**

Import the project as an existing maven projekt into your IDE, compile with Maven (men clean install), Junit tests are run automatically and should all work otherwise there could be a problem with the project setup. Check compiler settings for Java (JDK).

**Dependencies**

This project uses Jodatime ver 2.7 from Apache to handle all dates (see pom.xml file in the project root folder). It is automatically installed by maven at build time. Jodatime ensures that the API can be used in both old and newer versions of Java. Joda-Time was the "de facto" standard date and time library for Java prior to Java SE 8. It may be so that this Joda time dependency will become deprecated in future versions of Personnummer. For more information, go to http://www.joda.org/joda-time/

** How to run tests **
Unit tests are based on JUnit.

** more facts **

* Checksum algorithm is based upon the Luhn-algorithm a.k.a modulus-10-algorithm.

* This API is based on documents publicly available at the Swedish Tax Agency: [http://www.skatteverket.se].

* This project was originally hosted on bitbucket 2015-09-27, but has moved to GitHub since 2020-02-19 *

* This project is licensed under the terms of the [GPL v3](https://www.gnu.org/licenses/gpl-3.0.txt *


### Who do I talk to? ###

Oskar Bergström (repo owner).
