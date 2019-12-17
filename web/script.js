let colors = ["white", "green", "orange", "red", "yellow", "blue"];
let selected = 0;
let currentColor = 1;
let SOLVED_CUBE = [5, 5, 5, 5, 2, 2, 2, 2, 0, 0, 0, 0, 3, 3, 3, 3, 1, 1, 1, 1, 4, 4, 4, 4];
let state = [5, 5, 5, 5, 2, 2, 2, 2, 0, 0, 0, 0, 3, 3, 3, 3, 1, 1, 1, 1, 4, 4, 4, 4];
let cube = document.getElementById("cube");
initializeState();

JSValue window = browser.executeJavaScriptAndReturnValue("window");
window.asObject().setProperty("java", new JavaObject());

function initializeState(){
    cube = document.getElementById("cube");
    let cubeChildren = cube.children;
    
    let faceNumber = 0;
    for(let i = 0; i < cubeChildren.length; i++){
        let e = cubeChildren[i];
        if(e.tagName == "DIV") continue;
        let trs = e.children[0].children;
        for(let j = 0; j < 2; j++){
            let tds = trs[j].children;
            tds[0].style.backgroundColor = colors[SOLVED_CUBE[faceNumber*4 + j*2]]; 
            tds[1].style.backgroundColor = colors[SOLVED_CUBE[faceNumber*4 + j*2+1]];
        }
        faceNumber++;
    }
}

function getState(){
    cube = document.getElementById("cube");
    let cubeChildren = cube.children;
    
    let faceNumber = 0;
    for(let i = 0; i < cubeChildren.length; i++){
        let e = cubeChildren[i];
        if(e.tagName == "DIV") continue;
        let trs = e.children[0].children;
        for(let j = 0; j < 2; j++){
            let tds = trs[j].children;
            state[faceNumber*4 + j*2] = colors.indexOf(tds[0].style.backgroundColor); 
            state[faceNumber*4 + j*2+1] = colors.indexOf(tds[1].style.backgroundColor);
        }
        faceNumber++;
    }
    return state;
}

function clicked(obj){
    obj.style.backgroundColor = colors[currentColor - 1];
}

function enlarge(obj){
    if(selected){
        selected.style.border = "1px solid black";
    }
    selected = obj;
    selected.style.border = "6px solid black";
}

function solve(){
    getState();
    var solver = Java.type("Test");
    console.log(solver.getText());
}

document.addEventListener('keydown', function(event) {
    
    if(event.keyCode > 48 && event.keyCode < 55) {
        currentColor = event.keyCode - 48;
        enlarge(document.querySelector("#colorsTable td:nth-child(" + currentColor + ")"));
    }
    
});