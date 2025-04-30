import React from 'react';
import { Paper, Typography, Box, List, ListItem, ListItemText, Divider, Chip, Stack } from '@mui/material';
import { ExpenseStats as ExpenseStatsType } from '../types/expense';
import { AttachMoney, TrendingUp } from '@mui/icons-material';

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
                <Stack direction="row" spacing={2} alignItems="center" sx={{ mb: 2 }}>
                    <AttachMoney color="primary" />
                    <Typography variant="subtitle1">
                        Total Expenses: <strong>${stats.total.toFixed(2)}</strong>
                    </Typography>
                </Stack>
                <Stack direction="row" spacing={2} alignItems="center">
                    <TrendingUp color="primary" />
                    <Typography variant="subtitle1">
                        Average Daily: <strong>${stats.averageDaily.toFixed(2)}</strong>
                    </Typography>
                </Stack>
            </Box>
            <Typography variant="subtitle1" gutterBottom>
                Top Expenses:
            </Typography>
            <List>
                {stats.topExpenses.map((expense, index) => (
                    <React.Fragment key={expense.id}>
                        <ListItem
                            sx={{
                                backgroundColor: 'grey.50',
                                borderRadius: 1,
                                mb: 1,
                                transition: 'all 0.3s ease',
                                '&:hover': {
                                    transform: 'scale(1.02)',
                                    boxShadow: 1,
                                    backgroundColor: 'grey.100'
                                }
                            }}
                        >
                            <ListItemText
                                primary={
                                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                                        <Chip 
                                            label={`#${index + 1}`}
                                            color="default"
                                            size="small"
                                            sx={{ 
                                                backgroundColor: 'grey.200',
                                                color: 'text.primary'
                                            }}
                                        />
                                        <Typography variant="subtitle1" component="span">
                                            {expense.category}
                                        </Typography>
                                    </Box>
                                }
                                secondary={
                                    <Typography 
                                        variant="h6" 
                                        color="text.primary"
                                        sx={{ mt: 1 }}
                                    >
                                        ${expense.amount.toFixed(2)}
                                    </Typography>
                                }
                            />
                        </ListItem>
                        {index < stats.topExpenses.length - 1 && <Divider />}
                    </React.Fragment>
                ))}
            </List>
        </Paper>
    );
}; 