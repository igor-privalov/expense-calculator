import React, { useState, useEffect } from 'react';
import { Container, CssBaseline, Box } from '@mui/material';
import { ExpenseForm } from './components/ExpenseForm';
import { ExpenseStats } from './components/ExpenseStats';
import { api } from './services/api';
import { ExpenseFormData, ExpenseStats as ExpenseStatsType } from './types/expense';

function App() {
    const [stats, setStats] = useState<ExpenseStatsType>({
        total: 0,
        averageDaily: 0,
        topExpenses: []
    });

    const fetchStats = async () => {
        try {
            const data = await api.getExpenseStats();
            setStats(data);
        } catch (error) {
            console.error('Error fetching stats:', error);
        }
    };

    useEffect(() => {
        fetchStats();
    }, []);

    const handleAddExpense = async (expense: ExpenseFormData) => {
        try {
            await api.addExpense(expense);
            fetchStats();
        } catch (error) {
            console.error('Error adding expense:', error);
        }
    };

    return (
        <CssBaseline>
            <Container maxWidth="md">
                <Box sx={{ my: 4 }}>
                    <ExpenseForm onSubmit={handleAddExpense} />
                    <ExpenseStats stats={stats} />
                </Box>
            </Container>
        </CssBaseline>
    );
}

export default App; 