[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![GitHub release](https://img.shields.io/github/release/k-tamura/easybuggy4kt.svg)](https://github.com/k-tamura/easybuggy4kt/releases/latest)

EasyBuggy Bootlin :bug:
=

EasyBuggy Bootlinは、Kotlinで開発されたSpring BootベースのEasyBuggyのクローンです。[EasyBuggy](https://github.com/k-tamura/easybuggy)は、[メモリリーク、デッドロック、JVMクラッシュ、SQLインジェクションなど](https://github.com/k-tamura/easybuggy4kt/wiki)、バグや脆弱性の動作を理解するためにつくられたバグだらけのWebアプリケーションです。

![logo](https://github.com/k-tamura/easybuggy4kt/blob/master/src/main/webapp/images/easybuggy.png)
![vuls](https://github.com/k-tamura/test/blob/master/bugs.png)

:clock4: クイックスタート
-

    $ gradle bootRun

( または[JVMオプション](https://github.com/k-tamura/easybuggy4kt/blob/master/pom.xml#L148)付きで ``` java -jar ROOT.war ``` か任意のサーブレットコンテナに ROOT.war をデプロイ。 )

以下にアクセス:

    http://localhost:8080


停止するには:

  <kbd>CTRL</kbd>+<kbd>C</kbd>をクリック
  

:clock4: 詳細は
-
   
[wikiページ](https://github.com/k-tamura/easybuggy4kt/wiki)を参照下さい。
