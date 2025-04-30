import React from 'react';
import { Paper, Typography, Box, List, ListItem, ListItemText, Divider } from '@mui/material';
import { ExpenseStats as ExpenseStatsType } from '../types/expense';

interface ExpenseStatsProps {
    stats: ExpenseStatsType;
}

export const ExpenseStats: React.FC<ExpenseStatsProps> = ({ stats }) => {
    return (
        <Paper elevation={3} sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
                Expense Statistics
            </Typography>
            <Box sx={{ mb: 3 }}>
                <Typography variant="subtitle1">
                    Total Expenses: ${stats.total.toFixed(2)}
                </Typography>
                <Typography variant="subtitle1">
                    Average Daily: ${stats.averageDaily.toFixed(2)}
                </Typography>
            </Box>
            <Typography variant="subtitle1" gutterBottom>
                Top Expenses:
            </Typography>
            <List>
                {stats.topExpenses.map((expense, index) => (
                    <React.Fragment key={expense.id}>
                        <ListItem>
                            <ListItemText
                                primary={expense.category}
                                secondary={`$${expense.amount.toFixed(2)}`}
                            />
                        </ListItem>
                        {index < stats.topExpenses.length - 1 && <Divider />}
                    </React.Fragment>
                ))}
            </List>
        </Paper>
    );
}; 