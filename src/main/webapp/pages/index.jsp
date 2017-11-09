<!DOCTYPE html>
<html>
  <head>
    <title>uploadimg.html</title>

    <meta name="keywords" content="keyword1,keyword2,keyword3"></meta>
    <meta name="description" content="this is my page"></meta>
    <meta name="content-type" content="text/html; charset=UTF-8"></meta>

    <!--<link rel="stylesheet" type="text/css" href="./styles.css">-->

  </head>

  <body>
  <p>Hello,${name} !</p>
  <form enctype="multipart/form-data" method="post" action="/testuploadimg1">
    <input type="file" name="file"/><br/>
    <input type="file" name="file"/><br/>
    <input type="file" name="file"/><br/>
    <input type="file" name="file"/><br/>
    <input type="submit" value="上传"/>
    </form>
  </body>
</html>