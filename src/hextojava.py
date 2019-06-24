def convert(hex):
    hex=hex.replace(" ","")
    
    for i in range (0,len(hex),2):
        value=0
        if hex[i] in ["a","b","c","d","e","f"]:
            for x in range(len(["a","b","c","d","e","f"])):
                if ["a","b","c","d","e","f"][x]==hex[i]:
                    value+=x+10
        else:
            value+=int(hex[i])
    

        
        value=value*16
        if hex[i+1] in ["a","b","c","d","e","f"]:
            for x in range(len(["a","b","c","d","e","f"])):
                if ["a","b","c","d","e","f"][x]==hex[i+1]:
                    value+=x+10
                    
        else:
            value+=int(hex[i+1])

    
        print("(byte) ",value, end=", ")
