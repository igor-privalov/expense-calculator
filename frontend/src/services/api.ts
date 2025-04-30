import axios from 'axios';
import { ExpenseFormData, ExpenseStats } from '../types/expense';

const API_BASE_URL = 'http://localhost:8080/api/v1';

export const api = {
    addExpense: async (expense: ExpenseFormData) => {
        const response = await axios.post(`${API_BASE_URL}/expenses`, expense);
        return response.data;
    },

    getExpenseStats: async (topCount: number = 3): Promise<ExpenseStats> => {
        const response = await axios.get(`${API_BASE_URL}/expenses/stats`, {
            params: { topCount }
        });
        return response.data;
    }
}; 