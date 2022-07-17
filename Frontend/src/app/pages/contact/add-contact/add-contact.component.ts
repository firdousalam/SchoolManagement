import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ContactQueryServiceService } from 'src/app/shared/services/api/contact-query-service.service';
import { IContact} from 'src/app/shared/models/contact';


@Component({
  selector: 'app-add-contact',
  templateUrl: './add-contact.component.html',
  styleUrls: ['./add-contact.component.scss']
})
export class AddContactComponent implements OnInit {
  form!: FormGroup;
  submitted = false;
  contactSubscription!:Subscription;
  constructor(private router:Router,private formBuilder: FormBuilder,private contactService:ContactQueryServiceService) { }

  ngOnInit(): void { this.form = this.formBuilder.group(
    {
      type: ['', Validators.required],
      queryOrClarification: ['', Validators.required],
      queryStatus: "pending",
      response: ""
    }
  );
}
  get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }
  onSubmit(): void {
    this.submitted = true;
    if (this.form.invalid) {
      return;
    }
    console.log(JSON.stringify(this.form.value, null, 2));
    const requestData:IContact = this.form.value;
    this.contactSubscription = this.contactService.create(requestData).subscribe((data:any)=>{
      this.router.navigate(['/contact'])
    },(error:Error)=>{
      console.log(error);
      
    })
  }
  onReset(): void {
    this.submitted = false;
    this.form.reset();
  }
}
