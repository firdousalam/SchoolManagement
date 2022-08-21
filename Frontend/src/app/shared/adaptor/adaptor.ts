import { isEmpty } from 'lodash';
export const applicationObjectCreation = (applicationObject: any, userId: any, applicationState: string, tempDocument?: any)=>{  
    return {
         application:{
             applicationState: applicationState,
             approvalStatus: applicationState,
             approverRemark: "",
             batchId: 1                       
         },        
         docList: [   
            ...applicationObject.docList         
           ],
         docTempList: [       
            ...tempDocument     
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
             approvalStatus: applicationState
         },
         stdEducationList:[
             ...applicationObject.stdEducationList
         ],
         stdProfession:{     
            ...applicationObject.stdProfessionList,
            organization: (!isEmpty(applicationObject.stdProfessionList))?applicationObject.stdProfessionList.organization:'',             
            designation: (!isEmpty(applicationObject.stdProfessionList))?applicationObject.stdProfessionList.designation:'',              
            placeOfDuty: (!isEmpty(applicationObject.stdProfessionList))?applicationObject.stdProfessionList.placeOfDuty:''            
         },       
         userId: userId
    }
 }
export const applicationObjectUpdate = (applicationObject: any, userId: any, applicationState: string, tempDocument?: any)=>{     
const studentProfileObj = JSON.parse(localStorage.getItem('studentProfileId')!);
let profileId = studentProfileObj.studentProfileId;    
return {
    application:{    
        ...applicationObject.application,
        updatedAt: new Date(),
        updatedBy: 'NA',
        profileId: applicationObject.id                       
    },        
    docList: [   
    ...applicationObject.docList         
    ],
    docTempList: [       
    ...tempDocument     
    ],        
    stdAdmission: {            
        applicationCategory: "",
        approvalStatus: "",
        batchNo: "",
        certificateLink: "",
        courseCenter: "",
        createdAt: "",
        createdBy: "",
        dateOfEntry: "",
        enrollmentNumber: "",
        id: 0,
        idCardNo: "",
        profileId: applicationObject.id,
        status: 1,
        updatedAt: "",
        updatedBy: "",
        validityTill: ""
    },  
    stdContactDetail:{
        ...applicationObject.stdContactDetail,   
        approvalStatus:"",
        signature: "",
        status: 1,
        updatedAt: new Date(),
        updatedBy: "NA", 
        profileId: applicationObject.id                
    },  
    stdPersonalDetail:{
        ...applicationObject.stdPersonalDetail,
        profileId: applicationObject.id,            
        updatedAt: new Date(),
        updatedBy: "NA",
        status: 1
    },
    stdEducationList:[
        ...applicationObject.stdEducationList                   
    ],
    stdProfession:{     
        ...applicationObject.stdProfessionList,
        organization: (!isEmpty(applicationObject.stdProfessionList))?applicationObject.stdProfessionList.organization:'',             
        designation: (!isEmpty(applicationObject.stdProfessionList))?applicationObject.stdProfessionList.designation:'',              
        placeOfDuty: (!isEmpty(applicationObject.stdProfessionList))?applicationObject.stdProfessionList.placeOfDuty:'',
        profileId: applicationObject.id,
        status: 1,
        updatedAt: new Date(),
        updatedBy: '',
        approvalStatus: "",
        createdAt: "",
        createdBy: "",
        dateOfEntry: "",
        id: 0            
    },       
    userId: userId.toString(),                
    status: applicationObject.status,
    docMetaDataList: [...applicationObject.docMetaDataList],
    createdAt: applicationObject.createdAt,
    createdBy: 'NA',
    updatedAt: new Date().toString(),
    updatedBy: 'NA',
    id: applicationObject.id      
}
}
 