export const setSession = (property: string, value: string): void=>{
    localStorage.setItem(property, value);    
}

export const getSession = (property: string)=>{
    const sessonValue = localStorage.getItem(property)?.toString();
    return sessonValue;
}
export const destroySession = (property: string)=>{
    localStorage.removeItem(property);
}