<#macro head title>
    <head>
        <link rel="icon" type="image/png" href="/images/favicon.ico">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title> ${title} </title>
        <link rel="stylesheet" href="/css/styles.css">
    </head>
</#macro>
<#macro header>
    <header>
        <div class="banner"></div>
    </header>
</#macro>
<#macro menu>
    <section>
        <div class="menu">
            <ul>
                <li>
                    <span><a href="/home">Home</a></span>
                </li>
                <li>
                    <span><a href="/products">Products</a></span>
                </li>
                <li>
                    <span><a href="/products/add">Add Product</a></span>
                </li>
            </ul>
        </div>
    </section>
</#macro>
<#macro content>
    <div class="content">
        <#nested>
    </div>
</#macro>
<#macro footer>
    <div class="footer">
        <p>Copyright © 2023 by Serhii. All rights reserved.</p>
    </div>
</#macro>