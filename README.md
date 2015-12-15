ClassfileVersionChecker
====

## 概要 / About

指定された jar ファイルやディレクトリ配下に特定バージョン以上でコンパイルされた class ファイルが含まれていないかチェックする簡易ツールです。


バイナリ配布されているオープンソース Java ライブラリを使うときに頻発する実行環境とコンパイル環境の違いによる不具合、UnsupportedClassVersionError とおさらばしたい。

No more java.lang.UnsupportedClassVersionError!

This tool gives you easily checking jar file which has Java class file which are compiled for newer JRE you suppose.


[ダウンロード / Download](https://github.com/x-nagasawa/ClassfileVersionChecker/releases)


## ライセンス / License

MIT License
Copyright (c) 2015 NagasawaXien

## 使い方

    > java -jar ClassfileVersionChecker.jar (バージョン番号) (検査対象のファイル名orパス)

- 例1 : jar ファイルに Java1.4 超を対象としてコンパイルされたファイルが含まれていないか確認する場合。(ファイルバージョン 48)


        > ClassfileVersionChecker.jar 48 ClassfileVersionChecker.jar
         ClassfileVersionChecker.jar
         com/github/x_nagasawa/cvc/ClassfileVersionChecker.class is compiled in J2SE 5.0

Java5 以降を対象にしたファイルが含まれていることが分かります。

- 例2 : libs ディレクトリ以下に JDK7 超を対象としてコンパイルされたファイルが含まれていないか確認する場合。

        > ClassfileVersionChecker.jar 51 ./libs
         ./libs/slf4j-api-1.7.13.jar
         ./libs/jetty-http-9.3.6.v20151106.jar
         org/eclipse/jetty/http/BadMessageException.class is compiled in J2SE 8.0
         org/eclipse/jetty/http/DateGenerator$1.class is compiled in J2SE 8.0
         org/eclipse/jetty/http/DateGenerator.class is compiled in J2SE 8.0
         org/eclipse/jetty/http/DateParser$1.class is compiled in J2SE 8.0
         org/eclipse/jetty/http/DateParser.class is compiled in J2SE 8.0
         (snip)

slf4j-api-1.7.13.jar には含まれておらず、jetty-http-9.3.6.v20151106.jar は Java8 以降を対象にしていることが分かります。


(バージョン番号) には実行環境のバージョン番号を指定します。
以下の通り、 class ファイルのバージョンでの指定になります。

- 45 JDK 1.1
- 46 JDK 1.2
- 47 JDK 1.3
- 48 JDK 1.4
- 49 J2SE 5.0
- 50 J2SE 6.0
- 51 J2SE 7.0
- 52 J2SE 8.0

(検査対象のファイル名orパス) は、文字通り検査対象となる class ファイル or jar ファイル or パス を指定します。


## Usage

    > java -jar ClassfileVersionChecker.jar (under limit version) (target file or path)

- Ex1) Check a jar file contains too new classfile

        > ClassfileVersionChecker.jar 48 ClassfileVersionChecker.jar
         ClassfileVersionChecker.jar
         com/github/x_nagasawa/cvc/ClassfileVersionChecker.class is compiled in J2SE 5.0

 It shows class files which are compiled greater than JDK1.4 (48) in ClassfileVe
rsionChecker.jar

- Ex2) Check jar files in a directory contain too new classfile

        > ClassfileVersionChecker.jar 51 ./libs
         ./libs/slf4j-api-1.7.13.jar
         ./libs/jetty-http-9.3.6.v20151106.jar
         org/eclipse/jetty/http/BadMessageException.class is compiled in J2SE 8.0
         org/eclipse/jetty/http/DateGenerator$1.class is compiled in J2SE 8.0
         org/eclipse/jetty/http/DateGenerator.class is compiled in J2SE 8.0
         org/eclipse/jetty/http/DateParser$1.class is compiled in J2SE 8.0
         org/eclipse/jetty/http/DateParser.class is compiled in J2SE 8.0
         (snip)

 It shows class files which are compiled greater than J2SE7 (51) in ./libs
 
 slf4j-api-1.7.13.jar has no over version files. But jetty-http-9.3.6.v20151106.jar has over version files for Java8 or later.

The argument (under limt verion) is under limit version of classfile your acceptable like as follow

- 45 JDK 1.1
- 46 JDK 1.2
- 47 JDK 1.3
- 48 JDK 1.4
- 49 J2SE 5.0
- 50 J2SE 6.0
- 51 J2SE 7.0
- 52 J2SE 8.0

The argument (target file or path) is *.jar or *.class or directory
