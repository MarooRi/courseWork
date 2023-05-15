package styles

object Styles {
    const val tableStyle = """
           
                table {
                    width:100%;
                    border-collapse: collapse;
                    border: 2px solid rgb(200,200,200);
                    letter-spacing: 1px;
                }
                tr {
                    width: 100%;
                    height: 100%;
                    border: 1px solid rgb(190,190,190);
                }
                
                td {
                 text-align: center;
                    min-width: 150px;
                    padding: 10px 20px;
                    border: 1px solid rgb(190,190,190);
                }
              
                
                tr:nth-child(even) td {
                    background-color: rgb(250,250,250);
                }
        
                tr:nth-child(odd) td {
                    background-color: rgb(245,245,245);
                }
            """

    const val selectStyle = """ select {
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
    const val animation = """body{
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
}