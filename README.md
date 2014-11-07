## GPART 소개 ( Introduce ) 
![](http://dev.naver.com/wiki/gpart/pds/FrontPage/1.png)

( 우분투 리눅스(ubuntu)에서 돌아가는 모습 )

모든 데스크탑 플랫폼에 돌아가는 티커 형식의 RSS 구독기입니다.

처음 이 프로그램은 학교내 공모전 출품작으로 리눅스 그놈 패널에서만 동작하도록 만들어진 패널 애플릿이였지만

( 0.1 버젼은 perl과 gtk를 이용해서 조잡하게 만들어졌습니다. 공동 3등 수상작... )

JAVA [SWT](http://www.eclipse.org/swt/ ) 기반으로 다시 만들어져 모든 데스크탑 운영체제( All Desktop OS)에서 사용할 수 있습니다.

프로그램에 대해 간단히 묘사하자면 사용자가 RSS 구독하면 프로그램이 피드를 읽어와 

JAVA [뉴스 티커](http://en.wikipedia.org/wiki/News_ticker)처럼 화면에 나타나는 형식입니다. 

( 비슷한 프로그램으로  [newsrssticker] (http://www.newsrssticker.com/index.php )와 [KNewsTicker](http://userbase.kde.org/KNewsTicker ) 가 있습니다. )

RSS 1.0과 2.0 버젼이 구독 가능하며 트위터(Twitter)의 글도 구독이 가능합니다.

추가로 트레이 기능이 제공되어 티커 형식 말고도 트레이를 통해서도 구독이 가능합니다.

( 비슷한 프로그램으로 [yassr](http://yarssr.sourceforge.net/)가 있습니다. )

##사용법 ( Tutorial )
### 티커 기본 조작 방법 ( Ticker Basic Control )
![](http://dev.naver.com/wiki/gpart/pds/FrontPage/2_1.png)

 * 마우스 오른쪽 버튼 클릭 + 드래그  =>  티커 이동 
( Mouse Right Button Clcik + Drag => Ticker moving )

 * 마우스 왼쪽 버튼 클릭 => 피드에 연결 
( Mouse Left Button Clcik  =>  Linking to feed )

 * 마우스 휠 굴림 =>  피드 횡 이동 
( Mouse Wheel Scroll  =>  Feed scroll )

 * 마우스 위에 올림  =>  피드 내용 표시 
(  Mouse Hover  => Feed content show )

 * 티커 오른쪽 부분에서 마우스 오른쪽 버튼 클릭 + 드래그 => 티커 크기 조정 
( The right side of ticker + Mouse Right Button Clcik + Drag  => Ticker resize )

### 트레이 기본 조작 방법 ( Tray Basic Control )
![](http://dev.naver.com/wiki/gpart/pds/FrontPage/3_1.png)
 * 마우스 왼쪽 버튼 클릭 =>  티커 숨김
( Mouse Left Button Clcik => Ticker Hide )
 * 마우스 오른쪽 버튼 클릭 =>  메뉴 보기 
( Mouse Right Button Clcik => Menu Show )

### 트위터 추가하기 ( Adding Twitter )

![](http://dev.naver.com/wiki/gpart/pds/FrontPage/4.png)

 * 마우스 오른쪽 버튼으로 클릭 트레이 클릭  ->  메뉴에서 환경설정 선택
( Click Right-click on the tray  ->  Select Preferences from the menu )

![](http://dev.naver.com/wiki/gpart/pds/FrontPage/5.png)

 * 환경 설정에서 '피드 아이템 클릭' 
( Preferences, click on the 'feed item' )

![](http://dev.naver.com/wiki/gpart/pds/FrontPage/6_1.png)

![](http://dev.naver.com/wiki/gpart/pds/FrontPage/6_2.png)

![](http://dev.naver.com/wiki/gpart/pds/FrontPage/6-3_2.png)

 * 트위터 아이디나 피드 주소를 입력. : 예를 들면 트위터 아이디 'minsoogo'
( Input Twitter ID or Feed URL  : example twitter id 'minsoogo' )

![](http://dev.naver.com/wiki/gpart/pds/FrontPage/7_1.png)

## 설치방법 ( Download and Install )

 * 프로그램이 자바로 작성되었기 때문에 [자바 실행 버젼]( http://www.oracle.com/technetwork/java/javase/downloads/index.html )을 먼저 설치하시기 바랍니다.

 * [윈도우 버젼(Window version)](http://dev.naver.com/projects/gpart/download/3609?filename=Setup.exe)은 그냥 받으셔서 설치하시면 됩니다.

 * [리눅스 버젼(Linux version)](http://dev.naver.com/projects/gpart/download/3610?filename=gpart-rss_0.2.tar.gz )은 ALL 버젼의 경우 

  설치(Insert)는' sudo make install '로 설치하시면 됩니다. 

  삭제(Delete)는' sudo make uninstall ' 명령으로 합니다.

 * [데비안(Debian)] (http://dev.naver.com/projects/gpart/download/3611?filename=gpart-rss_0.2-1_i386.deb)경우 

  설치(Insert)는 ' sudo dpkg -i gpart-rss_0.2-1_i386.deb '로 설치하시면 됩니다.

  삭제(Delete)는 ' sudo dpkg -r gpart-rss ' 명령으로 합니다.

 * [레드헷(Redhat)] (http://dev.naver.com/projects/gpart/download/3612?filename=gpart-rss-0.2-1.i386.rpm)경우 

  설치(Insert)는' sudo rpm -Uvh gpart-rss-0.2-1.i386.rpm '로 설치하시면 됩니다.

  삭제(Delete)는 ' sudo rpm -e gpart-rss ' 명령으로 합니다.

 * 참고로 Linux  gnome 2.X 의 경우 아래 스샷처럼 메뉴에서 바로가기를 찾으실 수 있습니다.
![](http://dev.naver.com/wiki/gpart/pds/FrontPage/11.png )

* Linux 상에서는 Console에 ' java -jar /usr/share/GPART/gpart.jar ' 을 입력하셔서 실행(excute)하시면 됩니다. 

## 프로젝트 참여방법 ( Project )
 * 그냥 프로그램의 버그를 알려주시거나 사용 소감을 게시판에 써주시는 것 만으로도  큰 도움이 됩니다.

 * 만일 같이 코딩하실 의향이 있으시다면 저한테 메일을 보내주시면 됩니다! 
 ( rootroot2@gmail.com ) 

## 라이선스 ( License )
The MIT License

Copyright (c) 2011 minsoo go

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

