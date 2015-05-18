
var host_url = "http://localhost:8080";

function loadAllDowgs(){
    var xmlhttp;
    if (window.XMLHttpRequest)
    {
        xmlhttp=new XMLHttpRequest();        
    }
    else
    {
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }    
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState===4 && xmlhttp.status===200)
        {
            var fullText = xmlhttp.responseText;
            var listOfDogs = JSON.parse(fullText).dogs;
            var dogHTML = "";
            for (i = 0; i<listOfDogs.length; i++){
                dowg = listOfDogs[i];
                dogHTML += "<div id=\"dog-item\" class=\"bar\">";
                dogHTML += "<img src=\"dowg.png\" class=\"dog-image\">";
                dogHTML += "<div id=\"dog-details\">";
                dogHTML += "<strong>Name: " + dowg.name + "</strong><br>";
                dogHTML += "Age: " + dowg.age + "<br>";
                dogHTML += "Breed: " + dowg.breed + "<br>";
                dogHTML += "Description: " + dowg.description + "<br>";
                dogHTML += "</div></div><br>";
            }
            document.getElementById("dowg-list").innerHTML=dogHTML;
        }
    };    
    xmlhttp.open("GET", host_url+"/alldogs/v1/", true);
    xmlhttp.setRequestHeader("Content-type", "application/json");
    xmlhttp.send();    
}

function loadDowg(){
    var xmlhttp;
    if (window.XMLHttpRequest)
    {
        xmlhttp=new XMLHttpRequest();        
    }
    else
    {
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState===4 && xmlhttp.status===200)
        {
            var fullText = xmlhttp.responseText;
            var jsonVer = JSON.parse(fullText);
            var name = jsonVer.name;
            var description = jsonVer.description;
            var age = jsonVer.age;
            var breed = jsonVer.breed;
            document.getElementById("doge-name").innerHTML=name;
            document.getElementById("doge-age").innerHTML=age;
            document.getElementById("doge-breed").innerHTML=breed;
            document.getElementById("doge-description").innerHTML=description;            
        }
    };    
    xmlhttp.open("GET", host_url+"/somedogs/v1/"+"james", true);
    xmlhttp.setRequestHeader("Content-type", "application/json");
    xmlhttp.send();    
}

function addDowg(){
    //addDowgFull("disclosure5","dance","2","groove on");
    addDowgFull(document.getElementById("dowg-name").value,
        document.getElementById("dowg-breed").value,
        document.getElementById("dowg-age").value,
        document.getElementById("dowg-description").value);
}

function addDowgFull(name, breed, age, description){
    var xmlhttp;
    if (window.XMLHttpRequest)
    {
        xmlhttp=new XMLHttpRequest();        
    }
    else
    {
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState===4 && xmlhttp.status===201)
        {
            document.getElementById("submission-status").innerHTML="Submission Received";
        }
    };    
    xmlhttp.open("POST", host_url+"/createdog/v1/", true);
    xmlhttp.setRequestHeader("Content-Type", "application/json");    
    var jsonBody = JSON.stringify({name:name, breed:breed, age:parseInt(age), description:description});    
    console.log(jsonBody);
    xmlhttp.send(jsonBody);
}

function deleteDowg(){
    deleteDowgFull(document.getElementById("dowg-name").value);
}

function deleteToggle(){
    var button = document.getElementById("dowg-button");
    if (button.disabled){
        button.disabled=false;
    } else {
        button.disabled=true;
    }
}

function deleteDowgFull(name){
    var xmlhttp;
    if (window.XMLHttpRequest)
    {
        xmlhttp=new XMLHttpRequest();        
    }
    else
    {
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState===4 && xmlhttp.status===200)
        {
            document.getElementById("submission-status").innerHTML="Dowg Deleted. You Big Meanie.";
        }
    };
    xmlhttp.open("DELETE", host_url+"/deletedogs/v1/"+name, true);
    xmlhttp.send();
}

function updateDowg(){
    updateDowgFull(document.getElementById("dowg-name").value,
    document.getElementById("dowg-breed").value,
    document.getElementById("dowg-age").value,
    document.getElementById("dowg-description").value);
}

function updateDowgFull(name, breed, age, description){
    var xmlhttp;
    if (window.XMLHttpRequest)
    {
        xmlhttp=new XMLHttpRequest();        
    }
    else
    {
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState===4 && xmlhttp.status===200)
        {
            document.getElementById("submission-status").innerHTML="Dowg Data Delivered!";
        }
    };
    xmlhttp.open("PUT", host_url+"/updatedog/v1/", true);
    xmlhttp.setRequestHeader("Content-Type", "application/json");    
    var jsonBody = JSON.stringify({name:name, breed:breed, age:parseInt(age), description:description});    
    console.log(jsonBody);
    xmlhttp.send(jsonBody);
}

