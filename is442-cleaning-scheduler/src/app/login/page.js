"use client";
import React, { useState } from 'react'; 
import { Checkbox } from 'primereact/checkbox';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';

export default function Login() {
    const [checked, setChecked] = useState(false); // Define the checked state
    return (
        <div className="flex align-items-center justify-content-center" style={{ minHeight: '100vh', backgroundColor: 'var(--primary-color)' }}>
            <div className="surface-card p-4 shadow-2 border-round w-full lg:w-6">
                <div className="text-center mb-5">
                    <img src="https://i0.wp.com/homecleaningsg.com/wp-content/uploads/2022/12/header-logo.webp?fit=249%2C47&ssl=1" alt="hyper" height={50} className="m-auto p-5" />
                    <div className="text-900 text-3xl font-medium mb-3">Login to Admin Console</div>
                </div>
                <div>
                    <label htmlFor="email" className="block text-900 font-medium mb-2">Email</label>
                    <InputText id="email" type="text" placeholder="Email address" className="w-full mb-3" />

                    <label htmlFor="password" className="block text-900 font-medium mb-2">Password</label>
                    <InputText id="password" type="password" placeholder="Password" className="w-full mb-3" />

                    <div className="flex align-items-center justify-content-between mb-6">
                        <div className="flex align-items-center">
                            <Checkbox id="rememberme" onChange={e => setChecked(e.checked)} checked={checked} className="mr-2" />
                            <label htmlFor="rememberme">Remember me</label>
                        </div>
                        <a className="font-medium no-underline ml-2 text-blue-500 text-right cursor-pointer">Forgot your password?</a>
                    </div>

                    <Button label="Sign In" icon="pi pi-user" className="w-1/2" />
                </div>
            </div>
        </div>

    );
}
