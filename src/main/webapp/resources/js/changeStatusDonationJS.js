let formTakenFromMe = document.getElementById("takenFromMe");

formTakenFromMe.addEventListener("change", function (){
    console.log(formTakenFromMe.checked);
    if(formTakenFromMe.checked){
        let givingDate = document.createElement("input");
        givingDate.setAttribute("type", "date");
        givingDate.setAttribute("path", "takenFromMeDate");
        let newLine = document.createElement("br");
        formTakenFromMe.parentElement.insertBefore(newLine, formTakenFromMe.parentElement.children[1]);
        formTakenFromMe.parentElement.insertBefore(givingDate, formTakenFromMe.parentElement.children[2]);
    }
    if(!formTakenFromMe.checked){
        if (formTakenFromMe.parentElement.children.length === 5){
            formTakenFromMe.parentElement.children[1].parentElement.removeChild(formTakenFromMe.parentElement.children[1]);
            formTakenFromMe.parentElement.children[1].parentElement.removeChild(formTakenFromMe.parentElement.children[1]);

        }
        console.log(formTakenFromMe.parentElement.children.length);
    }
});