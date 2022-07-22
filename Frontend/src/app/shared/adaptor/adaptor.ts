export const applicationObjectCreation = (applicationObject: any, userId: any)=>{   

   return {
        application:{
            applicationState: "Submitted",
            approvalStatus: "",
            approverRemark: "",
            batchId: 1           
        },
        docList: [            
          ],
        docTempList: [            
        ],        
        stdAdmission: {            
            applicationCategory: "",           
            batchNo: "",
            certificateLink: "",
            courseCenter: "",
            enrollmentNumber: "",            
            idCardNo: "",            
            validityTill: "",
            dateofEntry:"",
            id: null
          },  
        stdContactDetail:{
            ...applicationObject.stdContactDetail,   
            approvalStatus:""        
        },  
        stdPersonalDetail:{
            ...applicationObject.personalDetails,
            dateofEntry:"",
            employmentStatus:"Employed"
        },
        stdEducationList:[
            ...applicationObject.stdEducationList
        ],
        stdProfession:{
            ...applicationObject.stdProfessionList            
        },       
        userId: userId
   }
}
