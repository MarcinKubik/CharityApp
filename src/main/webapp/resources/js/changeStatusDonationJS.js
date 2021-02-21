let takenFromMeCheckbox = document.getElementById("takenFromMe");

takenFromMeCheckbox.addEventListener("change", function (){
    console.log(takenFromMeCheckbox.parentElement.children.length);
    if(takenFromMeCheckbox.checked){

        let submit = document.createElement("button");
        submit.setAttribute("type", "submit");
        submit.classList.add("btn-facebook");
        submit.innerText = "Zatwierdź oddanie daru";
        submit.setAttribute("id", "submitId");
        takenFromMeCheckbox.parentElement.appendChild(submit);

    }
    if(!takenFromMeCheckbox.checked){

        if (takenFromMeCheckbox.parentElement.children.length === 22 || takenFromMeCheckbox.parentElement.children.length === 23){ // with fielderror or without

            let submit = document.getElementById("submitId");
            submit.parentElement.removeChild(submit);

        }
        console.log(takenFromMeCheckbox.parentElement.children.length);
    }
});
