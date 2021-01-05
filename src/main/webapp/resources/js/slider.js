/*const html = document.querySelector("html");
html.classList.add("slider");
document.addEventListener("DOMContentLoaded", evt => {
    const prev = document.getElementsByClassName("btn prev-step");
    const next = document.getElementsByClassName("btn next-step");
    const ul = document.getElementsByClassName("help--slides active");
    prev.addEventListener("click", function (){
        ul.classList.remove("visible");
    });
});*/
/*const html = document.querySelector("html");
html.classList.add("slider");*/
document.addEventListener("DOMContentLoaded", function (e){
    const prev = document.querySelector("#prev");
    const next = document.querySelector("#next");
    const ul = document.querySelector(".help--slides active");
    prev.addEventListener("click", function (){
       this.style.display = "none";
    });
});
