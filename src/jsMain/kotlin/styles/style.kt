package styles

object Styles {
    const val tableStyle = """
                table {
                    border-collapse: collapse;
                    border: 2px solid rgb(200,200,200);
                    letter-spacing: 1px;
                    font-size: 0.8rem;
                }
                tr, td {
                    border: 1px solid rgb(190,190,190);
                    padding: 10px 20px;
                }
                tr {
                    background-color: rgb(235,235,235);
                }
        
                td {
                    text-align: center;
                } 
        
                tr:nth-child(even) td {
                    background-color: rgb(250,250,250);
                }
        
                tr:nth-child(odd) td {
                    background-color: rgb(245,245,245);
                }
       
                caption {
                    padding: 10px;
                }
            """
    const val buttonStyle = """
        div{
            text-align: center;
            padding: 10px;
        }
        
        button{
         cursor: pointer;
         fontFamily: monospace;
         background-color: pink;
         font-size: 20px;
         padding: 20px;
         border: none;
        }
         button:hover{
            background-color: rgb(230, 156, 156);
        }
    """
    const val selectStyle = """ 
        select {
                  background-color: #f2f2f2;
                  color: #333;
                  border: 1px solid #ccc;
                  border-radius: 5px;
                  padding: 10px;
                  box-shadow: 0 0 5px rgba(0,0,0,0.1);
                  width: 200px;
                  margin: 10px;
                }    
                
                select:hover, select:focus {
                  outline: none;
                  border-color: #66afe9;
                  box-shadow: 0 0 5px rgba(102,175,233,0.5);
                }
                
                select::-ms-expand {
                  display: none;
                }     
"""

val selStyle = """
  select {
  width: 40%;
  height: 40px;
  padding: 6px 45px 6px 15px;
  appearance: none;
  -moz-appearance: none;
  -webkit-appearance: none;
  text-indent: 0.01px;
  text-overflow: "";
}

select:arrow {
  position: absolute;
  top: 0;
  right: 0;
  padding: 8px 15px;
  pointer-events: none;
}

arrow:down {
    width: 0;
    height: 0;
    border-left: 5px solid transparent;
    border-right: 5px solid transparent;
    border-top: 10px solid #4A4A4A;
}

arrow:up {
  width: 0;
  height: 0;
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
  border-bottom: 10px solid #4A4A4A;
  margin-bottom: 3px;
}

"""

    val butStyle = """
  p{
 position: absolute;
        bottom: 60px;
        left: 50px;
}

 p form button{
  font-size:1.5rem;
  border:2px solid white;
  border-radius:100px;
  width:60px;
  cursor: pointer;
  fontFamily: monospace;
  background-color: pink;
  font-size: 20px;
  padding: 20px;
  }
p form button:hover{
  background-color: rgb(230, 156, 156);
  }

p form button:hover{
  width:155px;
  background-color: white;
   box-shadow: 0px 5px 5px rgba(0,0,0,0.2);
  transition:.3s;
}

p form button:active{
  box-shadow: 0px 2px 5px rgba(0,0,0,0.2);
  transition: .05s
}
    
    """

    val animat = """
        div{
        display: grid;
        place-items: center;
        }
h1{
  width: 22ch;
  animation: typing 2s steps(22), blink .5s step-end infinite alternate;
  white-space: nowrap;
  overflow: hidden;
  border-right: 3px solid;
  font-family: monospace;
  font-size: 2em;
}

@keyframes typing {
  from {
    width: 0
  }
}
    
@keyframes blink {
  50% {
    border-color: transparent
  }
}
    """
  //  position: relative;
    //position: absolute;
    //top: -150px;
    //left: 0;
   // justify-content: center;
   //flex-direction: column;
    //width: 200%;
  //  margin-top: 200px;
    val testStyle = """
  footer{
   position: absolute;
        bottom: -30px;
        left: 5px;
        right: 5px;
        top: 680px;
        height: 150px;
   text-align: center;
    justify-content: center;
    background-color: rgba(230, 156, 156, 0.3);
    display: flex;
    align-items: center;
    display: flex;
}

 footer li{
  margin-top: 20px;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 10px 0;
  list-style: none;
}

 footer li a {
  margin-top: 10px;
  color: black;
  font-size: 1.2em;
  opacity: 0.75;
  margin: 0 15px;
  text-decoration: none;

}
 footer li a:hover {
    opacity: 1;
}
footer label {
position: absolute;
        bottom: 0px;
        left: 0px;
        right: 0px;
        top: 110px;
  color: black;
  text-align: center;
  font-size: 1.1em;
}
          """

    const val animation = """
        body{
  min-height: 100vh;
  padding: 0px;
  margin: 0px;
}
.spin-wrapper{
  position: relative;
  width: 100%;
  height: 100vh;
  background: #ffffff;
  .spinner{
    position: absolute;
    height: 60px;
    width: 60px;
    border: 3px solid transparent;
    border-top-color: #A04668;
    top: 50%;
    left: 50%;
    margin: -30px;
    border-radius: 50%;
    animation: spin 2s linear infinite;
    
    &:before, &:after{
      content:'';
      position: absolute;
      border: 3px solid transparent;
      border-radius: 50%;
    }
    
    &:before{
      border-top-color: #254E70;
      top: -12px;
      left: -12px;
      right: -12px;
      bottom: -12px;
      animation: spin 3s linear infinite;
    }
    
    &:after{
      border-top-color: #FFFBFE;
      top: 6px;
      left: 6px;
      right: 6px;
      bottom: 6px;  
      animation: spin 4s linear infinite;
    }
  }
}
@keyframes spin{
  0% {transform: rotate(0deg);}
  100% {transform: rotate(360deg);}
}"""
    const val labStyle = """
#podskazka{
  position: relative;
  cursor: pointer;
}

#podskazka:hover:before{
   position: absolute;
   top:30px;
  width: 100px;
  padding: 5px;
  border-radius: 4px;
  bottom: 25px;
  left: 50px;
  border: 2px solid red;
   content: "если преподаватель в рамке, то он уже есть в бд";
}
        """
}
