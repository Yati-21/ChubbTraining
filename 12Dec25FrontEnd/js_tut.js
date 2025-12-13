// // console.log(132143*120);

// // console.log("Hello World");


// //JS- dynamic language
// //We dont need to specify data types of variables
// //JS automatically figures out the data type based on the value assigned

// //primitive data types in js /value types - variables that store data directly
// //string data type
// let x = "yatii";
// console.log(x);
// console.log(typeof x);

// //number data type
// // let a = 123;
// let a = 123.43;
// console.log(a);
// console.log(typeof a);


// //boolean data type
// let b = true;
// console.log(b);
// console.log(typeof b);

// //undefined data type
// let c;
// console.log(c);
// console.log(typeof c);


// // //we can do this but shouldnt
// // let xy = "yatii";
// // xy= underfined;


// // xy= null;   //preferred way to clear the value of a variable


// //---------------------------------------


//rereferencing data types / non-primitive data types - variables that store reference to the location in memory where the data is stored


//objects, function , arrays

// //object exaple
// let course={
//     name: "js tut",
//     duration: 3,
//     isFree: true
// };
// console.log(course);
// console.log(typeof course);
// console.log(course.name);
// console.log(course.duration);
// console.log(course.isFree);


//  //in case of primitive data types, when we assign a variable to another variable, a copy of the value is created -- COPY BY VALUE
// let x= "yatii";
// let y=x;
// x= "new value";
// console.log(x);
// console.log(y);  //y will still have the old value because strings are primitive data types


// //in case of rereferencing data types, when we assign a variable to another variable, a reference to the location in memory is created -- COPY BY REFERENCE
// let obj1={
//     name: "js tut",
//     duration: 3,
//     isFree: true
// };
// let obj2=obj1;
// obj1.name="new course name";
// console.log(obj1.name);
// console.log(obj2.name);  //obj2 will also reflect the change because objects are re-referencing data types



//// BOTH LENGTH AND TYPE CAN BE DYNAMIC IN ARRAYS
// // arrays example
// let arr1=[1,2,4,4];
// console.log(arr1);

// let arr2=["abc","def","ghi"];
// console.log(arr2[0]);
// console.log(arr2.length);

// let arr3=[1,"abc",true, null, undefined];
// console.log(arr3);
// console.log(typeof arr3);  //OBJECT type



// //function example
// function hello(name)
// {
//     console.log("Hello World:"+name);
//     console.log("Hello World:",name);
// }
// hello("Yatii");





//INTERESTING!!!!! - hoisting in js

//hoisting -- works only in var
// console.log(myVar);  
// var myVar= 10;
// // let myVar= 10; -- wont work
// // const myVar= 10; -- wont work 
// console.log(myVar);  


// hello("Yatii");  //can call function before defining function due to hoisting
// function hello(name)
// {
//     console.log("Hello World:"+name);
//     console.log("Hello World:",name);
// }


// console.log(xyz);
// var xyz= 100;
// console.log(xyz); 



// function test()
// {
//     let a= 10;
//     console.log(a);
// }
// console.log(a);  //error - a is not defined outside the function
// test();  //10



// console.log(a);  //error - a is not defined outside the function
// var a= 10;
// console.log(a);  //10
// console.log(this.a); //10 
// console.log(window.a); //10
// //this / window refers to the global object in browser environment

// console.log(window);
// console.log(this ===window);  //true



function add(a,b)
{
    return a+b;
}
const result=add(3,4,5)
console.log(result)