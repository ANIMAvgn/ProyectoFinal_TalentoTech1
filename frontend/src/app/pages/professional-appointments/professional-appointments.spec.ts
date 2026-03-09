import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfessionalAppointments } from './professional-appointments';

describe('ProfessionalAppointments', () => {
  let component: ProfessionalAppointments;
  let fixture: ComponentFixture<ProfessionalAppointments>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfessionalAppointments]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfessionalAppointments);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
