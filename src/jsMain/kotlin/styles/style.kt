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
}