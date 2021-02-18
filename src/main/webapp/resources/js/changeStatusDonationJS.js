let takenFromMeCheckbox = document.getElementById("takenFromMe");

takenFromMeCheckbox.addEventListener("change", function (){
    console.log(takenFromMeCheckbox.checked);
    if(takenFromMeCheckbox.checked){

        let givingDateLabel = document.createElement("label");
        givingDateLabel.setAttribute("id", "givingDateLabelId");
        givingDateLabel.setAttribute("for", "givingDateId");
        givingDateLabel.innerText = "Podaj datę";

        let givingDate = document.createElement("input");
        givingDate.setAttribute("type", "date");
        givingDate.setAttribute("path", "takenFromMeDateString");
        givingDate.setAttribute("id", "givingDateId");

        let submit = document.createElement("button");
        submit.setAttribute("type", "submit");
        submit.classList.add("btn-facebook");
        submit.innerText = "Zatwierdź oddanie daru";
        submit.setAttribute("id", "submitId");

        let newLine = document.createElement("br");

        takenFromMeCheckbox.parentElement.insertBefore(newLine, takenFromMeCheckbox.parentElement.children[13]);
        takenFromMeCheckbox.parentElement.insertBefore(givingDateLabel, takenFromMeCheckbox.parentElement.children[14]);
        takenFromMeCheckbox.parentElement.insertBefore(givingDate, takenFromMeCheckbox.parentElement.children[15]);
        takenFromMeCheckbox.parentElement.appendChild(submit);
        console.log(givingDate);
    }
    if(!takenFromMeCheckbox.checked){

        if (takenFromMeCheckbox.parentElement.children.length === 18){
            let givingDate = document.getElementById("givingDateId");
            console.log(givingDate.value)
            givingDate.parentElement.removeChild(givingDate);

            let givingDateLabel = document.getElementById("givingDateLabelId");
            givingDateLabel.parentElement.removeChild(givingDateLabel.previousElementSibling);
            givingDateLabel.parentElement.removeChild(givingDateLabel);

            let submit = document.getElementById("submitId");
            submit.parentElement.removeChild(submit);

        }
        console.log(takenFromMeCheckbox.parentElement.children.length);
    }
});
