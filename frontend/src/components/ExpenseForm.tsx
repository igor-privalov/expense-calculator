import React, { useState } from 'react';
import { TextField, Button, Box, Paper, Typography, Alert } from '@mui/material';
import { ExpenseFormData } from '../types/expense';

interface ExpenseFormProps {
    onSubmit: (data: ExpenseFormData) => void;
}

interface FormErrors {
    category?: string;
    amount?: string;
}

export const ExpenseForm: React.FC<ExpenseFormProps> = ({ onSubmit }) => {
    const [formData, setFormData] = useState<ExpenseFormData>({
        category: '',
        amount: 0
    });

    const [errors, setErrors] = useState<FormErrors>({});
    const [submitError, setSubmitError] = useState<string>('');

    const validateForm = (): boolean => {
        const newErrors: FormErrors = {};
        let isValid = true;

        // Category validation
        if (!formData.category.trim()) {
            newErrors.category = 'Category is required';
            isValid = false;
        } else if (formData.category.length < 2) {
            newErrors.category = 'Category must be at least 2 characters long';
            isValid = false;
        }

        // Amount validation
        if (formData.amount <= 0) {
            newErrors.amount = 'Amount must be greater than 0';
            isValid = false;
        } else if (formData.amount > 1000000) {
            newErrors.amount = 'Amount must be less than 1,000,000';
            isValid = false;
        }

        setErrors(newErrors);
        return isValid;
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name === 'amount' ? parseFloat(value) || 0 : value
        }));
        // Clear error when user starts typing
        if (errors[name as keyof FormErrors]) {
            setErrors(prev => ({ ...prev, [name]: undefined }));
        }
        setSubmitError('');
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (validateForm()) {
            try {
                onSubmit(formData);
                setFormData({ category: '', amount: 0 });
                setErrors({});
                setSubmitError('');
            } catch (error) {
                setSubmitError('Failed to generate expense. Please try again.');
            }
        }
    };

    return (
        <Paper elevation={3} sx={{ p: 3, mb: 3 }}>
            <Typography variant="h6" gutterBottom>
                Add New Expense
            </Typography>
            {submitError && (
                <Alert severity="error" sx={{ mb: 2 }}>
                    {submitError}
                </Alert>
            )}
            <Box component="form" onSubmit={handleSubmit} noValidate>
                <TextField
                    fullWidth
                    label="Category"
                    name="category"
                    value={formData.category}
                    onChange={handleChange}
                    margin="normal"
                    required
                    error={!!errors.category}
                    helperText={errors.category}
                    inputProps={{ maxLength: 50 }}
                />
                <TextField
                    fullWidth
                    label="Amount"
                    name="amount"
                    type="number"
                    value={formData.amount}
                    onChange={handleChange}
                    margin="normal"
                    required
                    error={!!errors.amount}
                    helperText={errors.amount}
                    inputProps={{ 
                        min: 0.01,
                        max: 1000000,
                        step: 0.01
                    }}
                />
                <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    fullWidth
                    sx={{ mt: 2 }}
                >
                    Generate
                </Button>
            </Box>
        </Paper>
    );
}; 