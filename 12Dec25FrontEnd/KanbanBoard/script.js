const tasks= document.querySelectorAll(".task");

const lists=document.querySelectorAll(".list")

for(const task of tasks)
{
    task.addEventListener("dragstart",dragStart)
    task.addEventListener("dragend",dragEnd)
}

for(const list of lists)
{
    list.addEventListener("dragover",dragOver)
    list.addEventListener("dragenter",dragEnter)
    list.addEventListener("dragleave",dragLeave)
    list.addEventListener("drop",dragDrop)
}

function dragStart(e){
    // e.preventDefault()
    e.dataTransfer.setData("text/plain",this.id)

}

function dragEnd(){
    console.log("drag ended")
}

function dragOver(e)
{
    e.preventDefault()
}

function dragEnter(e)
{
    e.preventDefault()
    this.classList.add("over")
}
function dragLeave(e)
{

    this.classList.remove("over")
}
function dragDrop(e)
{
    const id = e.dataTransfer.getData("text/plain")
    const task= document.getElementById(id)
    this.appendChild(task);
    this.classList.remove("over");
}
