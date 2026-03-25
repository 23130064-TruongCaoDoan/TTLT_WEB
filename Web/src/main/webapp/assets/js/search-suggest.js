let timer;

document.querySelectorAll(".search-input").forEach(input => {

    const box = input.closest("form").querySelector(".suggest-box");
    let index = -1;
    let usingKeyboard = false;

    async function loadHistory(){
        const res = await fetch("search-history");
        const data = await res.json();
        render(data);
    }

    function render(list){

        box.innerHTML="";
        index = -1;

        const keyword = input.value.trim();

        if(keyword !== ""){
            const first = document.createElement("div");
            first.className = "suggest-item suggest-input";
            first.textContent = input.value.trim();;

            first.onclick = ()=>{
                input.form.submit();
            };

            box.appendChild(first);
        }

        list.forEach(k=>{

            if(k.toLowerCase() === keyword.toLowerCase()) return;

            const div=document.createElement("div");
            div.className="suggest-item";
            div.textContent=k;

            div.onclick=()=>{
                input.value=k;
                input.form.submit();
            };

            box.appendChild(div);
        });

        if(box.children.length === 0){
            box.style.display="none";
            return;
        }

        box.style.display="block";
        index = -1;
    }

    function setActive(i){
        const items = box.querySelectorAll(".suggest-item:not(.suggest-input)");

        items.forEach(e => e.classList.remove("active"));

        if(items[i]){
            items[i].classList.add("active");
            index = i;
        }
    }

    input.addEventListener("focus", loadHistory);

    input.addEventListener("keydown", e=>{

        const items = box.querySelectorAll(".suggest-item:not(.suggest-input)");

        if(e.key === "ArrowDown" && items.length){
            index = (index + 1) % items.length;
            setActive(index);
            usingKeyboard = true;
            e.preventDefault();
        }

        else if(e.key === "ArrowUp" && items.length){
            index = (index - 1 + items.length) % items.length;
            setActive(index);
            usingKeyboard = true;
            e.preventDefault();
        }

        else if(e.key === "Enter"){
            if(usingKeyboard && index >= 0){
                e.preventDefault();
                input.value = items[index].textContent;
                input.form.submit();
            }
        }
    });

    document.addEventListener("click", e=>{
        if(!input.parentNode.contains(e.target)){
            box.style.display="none";
        }
    });

    async function loadSuggest(keyword){

        if(!keyword){
            loadHistory();
            return;
        }

        const res = await fetch("suggest?q=" + encodeURIComponent(keyword));
        const data = await res.json();
        render(data);
    }

    input.addEventListener("focus", ()=>{
        usingKeyboard = false;
        loadHistory();
    });

    input.addEventListener("input", e=>{
        usingKeyboard = false;
        index = -1;
        clearTimeout(timer);

        timer = setTimeout(()=>{
            loadSuggest(e.target.value.trim());
        },300);
    });
});