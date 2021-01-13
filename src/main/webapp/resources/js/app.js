document.addEventListener("DOMContentLoaded", function () {

    /**
     * Form Select
     */
    class FormSelect {
        constructor($el) {
            this.$el = $el;
            this.options = [...$el.children];
            this.init();
        }

        init() {
            this.createElements();
            this.addEvents();
            this.$el.parentElement.removeChild(this.$el);
        }

        createElements() {
            // Input for value
            this.valueInput = document.createElement("input");
            this.valueInput.type = "text";
            this.valueInput.name = this.$el.name;

            // Dropdown container
            this.dropdown = document.createElement("div");
            this.dropdown.classList.add("dropdown");

            // List container
            this.ul = document.createElement("ul");

            // All list options
            this.options.forEach((el, i) => {
                const li = document.createElement("li");
                li.dataset.value = el.value;
                li.innerText = el.innerText;

                if (i === 0) {
                    // First clickable option
                    this.current = document.createElement("div");
                    this.current.innerText = el.innerText;
                    this.dropdown.appendChild(this.current);
                    this.valueInput.value = el.value;
                    li.classList.add("selected");
                }

                this.ul.appendChild(li);
            });

            this.dropdown.appendChild(this.ul);
            this.dropdown.appendChild(this.valueInput);
            this.$el.parentElement.appendChild(this.dropdown);
        }

        addEvents() {
            this.dropdown.addEventListener("click", e => {
                const target = e.target;
                this.dropdown.classList.toggle("selecting");

                // Save new value only when clicked on li
                if (target.tagName === "LI") {
                    this.valueInput.value = target.dataset.value;
                    this.current.innerText = target.innerText;
                }
            });
        }
    }

    document.querySelectorAll(".form-group--dropdown select").forEach(el => {
        new FormSelect(el);
    });

    /**
     * Hide elements when clicked on document
     */
    document.addEventListener("click", function (e) {
        const target = e.target;
        const tagName = target.tagName;

        if (target.classList.contains("dropdown")) return false;

        if (tagName === "LI" && target.parentElement.parentElement.classList.contains("dropdown")) {
            return false;
        }

        if (tagName === "DIV" && target.parentElement.classList.contains("dropdown")) {
            return false;
        }

        document.querySelectorAll(".form-group--dropdown .dropdown").forEach(el => {
            el.classList.remove("selecting");
        });
    });

    /**
     * Switching between form steps
     */
    class FormSteps {
        constructor(form) {
            this.$form = form;
            this.$next = form.querySelectorAll(".next-step");
            this.$prev = form.querySelectorAll(".prev-step");
            this.$step = form.querySelector(".form--steps-counter span");
            this.currentStep = 1;

            this.$stepInstructions = form.querySelectorAll(".form--steps-instructions p");
            const $stepForms = form.querySelectorAll("form > div");

            this.slides = [...this.$stepInstructions, ...$stepForms];

            this.init();
        }

        /**
         * Init all methods
         */
        init() {
            this.events();
            this.updateForm();
        }

        /**
         * All events that are happening in form
         */
        events() {
            // Next step
            this.$next.forEach(btn => {
                btn.addEventListener("click", e => {
                    e.preventDefault();
                    if (this.validateForm()) {
                        this.currentStep++;
                        this.updateForm();
                    }
                });
            });

            // Previous step
            this.$prev.forEach(btn => {
                btn.addEventListener("click", e => {
                    e.preventDefault();
                    this.currentStep--;
                    this.updateForm();
                });
            });

            // Form submit
            this.$form.querySelector("form").addEventListener("submit", e => this.submit(e));
        }

        /**
         * Update form front-end
         * Show next or previous section etc.
         */
        updateForm() {
            this.$step.innerText = this.currentStep;

            // TODO: Validation

            this.slides.forEach(slide => {
                slide.classList.remove("active");

                if (slide.dataset.step == this.currentStep) {
                    slide.classList.add("active");
                }
            });

            this.$stepInstructions[0].parentElement.parentElement.hidden = this.currentStep >= 5;
            this.$step.parentElement.hidden = this.currentStep >= 5;

            // TODO: get data from inputs and show them in summary


            if (this.currentStep >= 5) {
                //inputs creating elements
                let categoriesFromForm = this.$form.querySelectorAll('[type="checkbox"]:checked');
                let bagsFromForm = this.$form.querySelector("#quantity");
                let fundationFromForm = this.$form.querySelector('[type="radio"]:checked');
                let streetFromForm = this.$form.querySelector("#street");
                let cityFromForm = this.$form.querySelector("#city");
                let zipCodeFromForm = this.$form.querySelector("#zipCode");
                let phoneNumberFromForm = this.$form.querySelector("#phoneNumber");
                let pickUpDateFromForm = this.$form.querySelector("#pickUpDate");
                let pickUpTimeFromForm = this.$form.querySelector("#pickUpTime");
                let pickUpCommentFromForm = this.$form.querySelector("#pickUpComment");

                //summary creating elements
                let categoriesSummary = "";
                let fundationSummary;
                let step1Step2Summary = this.$form.getElementsByClassName("summary--text");
                let addressSummary = this.$form.querySelectorAll(".form-section--column");
                let streetSummary = addressSummary[2].querySelector("ul").firstElementChild;
                let citySummary = streetSummary.nextElementSibling;
                let zipCodeSummary = citySummary.nextElementSibling;
                let phoneNumberSummary = zipCodeSummary.nextElementSibling;
                let pickUpDateSummary = addressSummary[3].querySelector("ul").firstElementChild;
                let pickUpTimeSummary = pickUpDateSummary.nextElementSibling;
                let pickUpCommentSummary = pickUpTimeSummary.nextElementSibling;


                //giving values for checkboses and radio

                categoriesFromForm.forEach(cat => {
                    categoriesSummary += " " + cat.nextElementSibling.nextElementSibling.nextElementSibling.innerText;
                });
                fundationSummary = fundationFromForm.nextElementSibling.nextElementSibling.firstElementChild.innerText.substr(8);

                //giving values for inputs
                step1Step2Summary[0].innerText = bagsFromForm.value + " worki " + categoriesSummary;
                step1Step2Summary[1].innerText = "Dla Fundacji " + fundationSummary + " w " + cityFromForm.value;
                streetSummary.innerText = streetFromForm.value;
                citySummary.innerText = cityFromForm.value;
                zipCodeSummary.innerText = zipCodeFromForm.value;
                phoneNumberSummary.innerText = phoneNumberFromForm.value;
                pickUpDateSummary.innerText = pickUpDateFromForm.value;
                pickUpTimeSummary.innerText = pickUpTimeFromForm.value;
                pickUpCommentSummary.innerText = pickUpCommentFromForm.value;

            }
        }

        validateForm() {
            if (this.currentStep === 1) {
                let categoriesFromForm = this.$form.querySelectorAll('[type="checkbox"]:checked');
                if (categoriesFromForm.length === 0) {
                    alert("Musisz wybrać co najmniej jedną kategorię");
                    return false;
                }
            }

            if (this.currentStep === 2) {
                let quantity = this.$form.querySelector("#quantity");
                if (quantity.value <= 0 || (quantity.value % 1 !== 0)) { //Number.isInteger(quantity.value) daje błędny wynik, dlaczego?
                    console.log(quantity.value);
                    alert("Musisz podać całkowitą liczbę worków większą od 0");
                    return false;
                }
            }

            if (this.currentStep === 3) {
                let radioButton = this.$form.querySelector('[type="radio"]:checked');
                if(radioButton == null){
                    alert("Musisz wybrać fundację");
                    return false;
                }
            }

            if (this.currentStep === 4) {
                let street = this.$form.querySelector("#street");
                if(street.value === ""){
                    alert("Musisz podać poprawną nazwę ulicy");
                    return false
                }
                console.log(street);

                let city = this.$form.querySelector("#city");
                let cityRegex = new RegExp("[A-ZÓŹŻĆŁŚ]{1}[a-zóżźćąęłśń]{2,}");
                if(city.value === "" || !cityRegex.test(city.value)){
                    alert("Musisz podać poprawną nazwę miasta");
                    return false
                }
                console.log(city)

                let zipCode = this.$form.querySelector("#zipCode");
                let phoneNumber = this.$form.querySelector("#phoneNumber");
                let pickUpDate = this.$form.querySelector("#pickUpDate");
                let pickUpTime = this.$form.querySelector("#pickUpTime");
                let pickUpComment = this.$form.querySelector("#pickUpComment");


            }

            return true;
        }


    }

    const form = document.querySelector(".form--steps");
    if (form !== null) {
        new FormSteps(form);
    }
});
